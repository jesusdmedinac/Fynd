package com.jesusdmedinac.fynd.domain.repository

import com.jesusdmedinac.fynd.domain.model.*
import kotlinx.coroutines.flow.Flow

interface HostRepository {
    suspend fun getCurrentSession(): Flow<Session>
    suspend fun getCurrentHost(): Host
    suspend fun signIn(signInUserCredentials: SignInUserCredentials): SignInResult
    suspend fun signUp(signUpUserCredentials: SignUpUserCredentials): SignUpResult
}