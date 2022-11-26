package com.jesusdmedinac.fynd.domain.repository

import com.jesusdmedinac.fynd.domain.model.*
import kotlinx.coroutines.flow.Flow

interface HostRepository {
    suspend fun retrieveCurrentSession(email: String)
    suspend fun getCurrentHostFlow(): Flow<Result<Host>>
    suspend fun getCurrentHost(): Result<Host>
    suspend fun signIn(signInUserCredentials: SignInUserCredentials): SignInResult
    suspend fun signUp(signUpUserCredentials: SignUpUserCredentials): SignUpResult
    suspend fun updateColumnsBy(leaderEmail: String, columns: String): Result<Unit>
    suspend fun updateRowsBy(leaderEmail: String, rows: String): Result<Unit>
}