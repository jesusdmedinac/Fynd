package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.local.HostDao
import com.jesusdmedinac.fynd.data.remote.model.HostUser as RemoteHostUser
import com.jesusdmedinac.fynd.data.remote.model.SignInHostUserCredentials
import com.jesusdmedinac.fynd.data.remote.HostRemoteDataSource
import com.jesusdmedinac.fynd.data.remote.model.SignUpHostUserCredentials
import com.jesusdmedinac.fynd.domain.model.*
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import com.jesusdmedinac.fynd.data.local.model.HostUser as LocalHostUser

class HostRepositoryImpl @Inject constructor(
    private val hostRemoteDataSource: HostRemoteDataSource,
    @Named("io-dispatcher")
    private val ioDispatcher: CoroutineDispatcher,
    private val hostDao: HostDao,
) : HostRepository {
    override suspend fun getCurrentSession(): Flow<Session> =
        hostRemoteDataSource.getCurrentSession()
            .flowOn(ioDispatcher)
            .map { it.toSession() }

    override suspend fun signIn(signInUserCredentials: SignInUserCredentials): SignInResult =
        withContext(ioDispatcher) {
            if (!hostDao.isLoggedIn(signInUserCredentials.email)) {
                val signInHostUserCredentials = signInUserCredentials.toSignInHostUserCredentials()
                runCatching { hostRemoteDataSource.signIn(signInHostUserCredentials) }
                    .onFailure { return@withContext SignInResult.UserDoesNotExists }
                    .onSuccess { remoteHostUser ->
                        val localHostUser = remoteHostUser.toLocalHostUser()
                        hostDao.insertHostUser(localHostUser)
                        return@withContext SignInResult.Success
                    }
            }
            SignInResult.UserAlreadyLoggedIn
        }

    override suspend fun signUp(signUpUserCredentials: SignUpUserCredentials): SignUpResult =
        withContext(ioDispatcher) {
            if (!hostDao.isLoggedIn(signUpUserCredentials.email)) {
                val signUpHostUserCredentials = signUpUserCredentials.toSignUpHostUserCredentials()
                runCatching { hostRemoteDataSource.signUp(signUpHostUserCredentials) }
                    .onFailure { return@withContext SignUpResult.Error(it) }
                    .onSuccess { remoteHostUser ->
                        val localHostUser = remoteHostUser.toLocalHostUser()
                        hostDao.insertHostUser(localHostUser)
                        return@withContext SignUpResult.Success
                    }
            }
            SignUpResult.Error(Throwable("User is Logged In"))
        }

    private fun SignInUserCredentials.toSignInHostUserCredentials() = SignInHostUserCredentials(
        email,
        password,
    )

    private fun SignUpUserCredentials.toSignUpHostUserCredentials() = SignUpHostUserCredentials(
        email,
        displayName,
        password,
    )

    private fun RemoteHostUser.toLocalHostUser() = LocalHostUser(
        email,
        photoUrl,
        displayName,
    )

    private fun RemoteHostUser?.toSession(): Session = this
        ?.let { Session.LoggedHost(Host(it.email, isLeader = false)) }
        ?: run { Session.HostIsNotLoggedIn }
}