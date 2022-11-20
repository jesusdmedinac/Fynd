package com.jesusdmedinac.fynd.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.jesusdmedinac.fynd.data.remote.mapper.MapToHostUserMapper
import com.jesusdmedinac.fynd.data.remote.mapper.PasswordStringHasher
import com.jesusdmedinac.fynd.data.remote.mapper.SignUpHostUserCredentialsToMapMapper
import com.jesusdmedinac.fynd.data.remote.model.HostUser
import com.jesusdmedinac.fynd.data.remote.model.SignInHostUserCredentials
import com.jesusdmedinac.fynd.data.remote.model.SignUpHostUserCredentials
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume


interface HostRemoteDataSource {
    suspend fun getHostUserBy(email: String): Flow<HostUser>
    suspend fun signIn(signInHostUserCredentials: SignInHostUserCredentials): HostUser
    suspend fun signUp(signUpHostUserCredentials: SignUpHostUserCredentials): HostUser
}

class HostRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val signUpHostUserCredentialsToMapMapper: SignUpHostUserCredentialsToMapMapper,
    private val passwordStringHasher: PasswordStringHasher,
    private val mapToHostUserMapper: MapToHostUserMapper,
) : HostRemoteDataSource {

    override suspend fun getHostUserBy(email: String): Flow<HostUser> = callbackFlow {
        firestore.collection("hosts")
            .document(email)
            .addSnapshotListener { nullableDocumentSnapshot, firebaseFirestoreException ->
                val documentSnapshot = nullableDocumentSnapshot ?: return@addSnapshotListener
                if (!documentSnapshot.exists()) return@addSnapshotListener
                val data = documentSnapshot.data ?: return@addSnapshotListener
                launch {
                    val hostUser = mapToHostUserMapper.map(data)
                    send(hostUser)
                }
            }
        awaitClose()
    }

    override suspend fun signIn(signInHostUserCredentials: SignInHostUserCredentials): HostUser =
        suspendCancellableCoroutine { continuation ->
            val emailFromCredential = signInHostUserCredentials.email
            firestore.collection("hosts")
                .document(emailFromCredential)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (!documentSnapshot.exists()) {
                        continuation.cancel(Throwable("Host with email $emailFromCredential not found"))
                    } else {
                        documentSnapshot.data?.let { data ->
                            val password = data["password"] as? String ?: ""
                            val passwordFromCredential = signInHostUserCredentials.password
                            val hashedPassword = passwordStringHasher(passwordFromCredential)
                            if (password != hashedPassword) {
                                continuation.cancel(Throwable("Invalid password for email $emailFromCredential"))
                            } else {
                                val hostUser = mapToHostUserMapper.map(data).copy(isLoggedIn = true)
                                continuation.resume(hostUser)
                            }
                        } ?: run {
                            continuation.cancel(Throwable("No Data for Host with email $emailFromCredential"))
                        }
                    }
                }
                .addOnFailureListener {
                    continuation.cancel(it)
                }
        }

    override suspend fun signUp(signUpHostUserCredentials: SignUpHostUserCredentials): HostUser {
        val generateQRCode = generateQRCode()
        val emailFromCredentials = signUpHostUserCredentials.email
        val signUpHostHashMap = signUpHostUserCredentialsToMapMapper
            .map(signUpHostUserCredentials)
            .toMutableMap()
            .run {
                set("qrCode", generateQRCode)
                toMap()
            }

        return suspendCancellableCoroutine { continuation ->
            firestore.collection("hosts")
                .document(emailFromCredentials)
                .set(signUpHostHashMap)
                .addOnSuccessListener {
                    continuation.resume(mapToHostUserMapper.map(signUpHostHashMap))
                }
                .addOnFailureListener {
                    continuation.cancel(it)
                }
        }
    }

    private suspend fun generateQRCode(): String {
        var qrCode = ""
        var validQRCode = false
        while (!validQRCode) {
            qrCode = (0..5)
                .map { ('A'..'Z').random() }
                .joinToString("")

            validQRCode = suspendCancellableCoroutine { continuation ->
                firestore.collection("hosts")
                    .whereEqualTo("qrCode", qrCode)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        continuation.resume(querySnapshot.documents.size == 0)
                    }
                    .addOnFailureListener {
                        continuation.cancel(it)
                    }
            }
        }
        return qrCode
    }
}