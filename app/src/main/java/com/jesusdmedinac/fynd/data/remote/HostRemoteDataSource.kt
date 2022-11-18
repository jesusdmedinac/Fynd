package com.jesusdmedinac.fynd.data.remote

import com.google.common.hash.HashFunction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesusdmedinac.fynd.data.mapper.FirebaseUserToHostUserMapper
import com.jesusdmedinac.fynd.data.remote.model.HostUser
import com.jesusdmedinac.fynd.data.remote.model.SignInHostUserCredentials
import com.jesusdmedinac.fynd.data.remote.model.SignUpHostUserCredentials
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume


interface HostRemoteDataSource {
    suspend fun signIn(signInHostUserCredentials: SignInHostUserCredentials): HostUser
    suspend fun signUp(signUpHostUserCredentials: SignUpHostUserCredentials): HostUser
}

class HostRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseUserToHostUserMapper: FirebaseUserToHostUserMapper,
    @Named("sha256")
    private val hashFunction: HashFunction,
) : HostRemoteDataSource {

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
                            val hashedPassword = passwordFromCredential.toHashedString()
                            if (password != hashedPassword) {
                                continuation.cancel(Throwable("Invalid password for email $emailFromCredential"))
                            } else {
                                val displayName: String = data["displayName"] as? String ?: ""
                                val email: String = data["email"] as? String ?: ""
                                val hostUser = HostUser(
                                    photoUrl = "",
                                    displayName = displayName,
                                    email = email,
                                )
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

    override suspend fun signUp(signUpHostUserCredentials: SignUpHostUserCredentials): HostUser =
        suspendCancellableCoroutine { continuation ->
            val emailFromCredential = signUpHostUserCredentials.email
            firestore.collection("hosts")
                .document(emailFromCredential)
                .set(
                    hashMapOf(
                        "photoUrl" to "",
                        "displayName" to signUpHostUserCredentials.displayName,
                        "email" to signUpHostUserCredentials.email,
                        "password" to signUpHostUserCredentials.password.toHashedString()
                    )
                )
                .addOnSuccessListener {
                    continuation.resume(signUpHostUserCredentials.toHostUser())
                }
                .addOnFailureListener {
                    continuation.cancel(it)
                }
        }

    private fun String.toHashedString(): String = with(hashFunction) {
        newHasher()
            .putString(this@toHashedString, Charsets.UTF_8)
            .hash()
            .toString()
    }

    private fun SignUpHostUserCredentials.toHostUser() = HostUser(
        "",
        displayName,
        email,
    )

    private fun ProducerScope<HostUser?>.trySendHostUser(auth: FirebaseAuth) {
        val hostUser = auth.getCurrentHostUser()
        trySendBlocking(hostUser)
    }

    private fun FirebaseAuth.getCurrentHostUser(): HostUser? = currentUser
        ?.let(firebaseUserToHostUserMapper::map)
}