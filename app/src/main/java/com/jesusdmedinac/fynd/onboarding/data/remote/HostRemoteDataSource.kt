package com.jesusdmedinac.fynd.onboarding.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jesusdmedinac.fynd.onboarding.data.mapper.FirebaseUserToHostUserMapper
import com.jesusdmedinac.fynd.onboarding.data.model.HostUser
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

interface HostRemoteDataSource {
    fun getCurrentSession(): Flow<HostUser?>
}

class HostRemoteDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseUserToHostUserMapper: FirebaseUserToHostUserMapper,
) : HostRemoteDataSource {
    override fun getCurrentSession(): Flow<HostUser?> = channelFlow {
        auth.addAuthStateListener {
            trySendHostUser(auth)
        }
        trySendHostUser(auth)
    }

    private fun ProducerScope<HostUser?>.trySendHostUser(auth: FirebaseAuth) {
        val hostUser = auth.getCurrentHostUser()
        println("dani $hostUser")
        trySendBlocking(hostUser)
    }

    private fun FirebaseAuth.getCurrentHostUser(): HostUser? = currentUser
        ?.let(firebaseUserToHostUserMapper::map)
}