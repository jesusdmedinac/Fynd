package com.jesusdmedinac.fynd.data.remote

import android.R.id
import com.google.common.hash.HashCode
import com.google.common.hash.HashFunction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesusdmedinac.fynd.data.mapper.FirebaseUserToHostUserMapper
import com.jesusdmedinac.fynd.data.remote.model.HostUser
import com.jesusdmedinac.fynd.data.remote.model.SignInHostUserCredentials
import com.jesusdmedinac.fynd.data.remote.model.SignUpHostUserCredentials
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
                        continuation.cancel(Exception("Host not found"))
                    } else {
                        val data = documentSnapshot.data
                        val displayName: String = data?.get("displayName") as? String ?: ""
                        val email: String = data?.get("email") as? String ?: ""
                        val hostUser = HostUser(
                            photoUrl = "",
                            displayName = displayName,
                            email = email,
                        )
                        continuation.resume(hostUser)
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
                        "password" to hashFunction.run {
                            newHasher()
                                .putString(signUpHostUserCredentials.password, Charsets.UTF_8)
                                .hash()
                                .toString()
                        }
                    )
                )
                .addOnSuccessListener {
                    continuation.resume(signUpHostUserCredentials.toHostUser())
                }
                .addOnFailureListener {
                    continuation.cancel(it)
                }
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