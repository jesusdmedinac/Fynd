package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.local.HostDao
import com.jesusdmedinac.fynd.data.remote.HostRemoteDataSource
import com.jesusdmedinac.fynd.data.remote.model.SignInHostUserCredentials
import com.jesusdmedinac.fynd.data.remote.model.SignUpHostUserCredentials
import com.jesusdmedinac.fynd.data.repository.mapper.RemoteHostUserToLocalHostUserMapper
import com.jesusdmedinac.fynd.domain.model.*
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import com.jesusdmedinac.fynd.data.local.model.HostUser as LocalHostUser
import com.jesusdmedinac.fynd.data.remote.model.HostUser as RemoteHostUser

class HostRepositoryImpl @Inject constructor(
    private val hostRemoteDataSource: HostRemoteDataSource,
    @Named("io-dispatcher")
    private val ioDispatcher: CoroutineDispatcher,
    private val hostDao: HostDao,
    private val remoteHostUserToLocalHostUserMapper: RemoteHostUserToLocalHostUserMapper
) : HostRepository {
    override suspend fun retrieveCurrentSession(email: String) {
        hostRemoteDataSource
            .getHostUserBy(email)
            .flowOn(ioDispatcher)
            .collect { remoteHostUser ->
                withContext(ioDispatcher) {
                    val localHostUser = remoteHostUserToLocalHostUserMapper
                        .map(remoteHostUser)
                        .copy(
                            isOnboardingWelcomeScreenViewed =
                            hostDao.isOnboardingWelcomeScreenViewed()
                        )
                    hostDao.insertHostUser(localHostUser)
                }
            }
    }

    override suspend fun getCurrentSession(): Flow<Session> = hostDao.loggedHostUserFlow()
        .flowOn(ioDispatcher)
        .map { it.toSession() }

    override suspend fun getCurrentHost(): Result<Host> = withContext(ioDispatcher) {
        val loggedHostUser = hostDao.loggedHostUser()
            ?: return@withContext Result.failure(Throwable("Current host is not available"))
        val loggedHost = loggedHostUser.toSession() as? Session.LoggedHost
            ?: return@withContext Result.failure(Throwable("Current host is not available"))
        Result.success(loggedHost.host)
    }

    override suspend fun signIn(signInUserCredentials: SignInUserCredentials): SignInResult =
        withContext(ioDispatcher) {
            if (!hostDao.isLoggedIn(signInUserCredentials.email)) {
                val signInHostUserCredentials = signInUserCredentials.toSignInHostUserCredentials()
                runCatching { hostRemoteDataSource.signIn(signInHostUserCredentials) }
                    .onFailure { return@withContext SignInResult.UserDoesNotExists }
                    .onSuccess { remoteHostUser ->
                        val localHostUser = remoteHostUser.toLocalHostUser().copy(isLoggedIn = true)
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
                        val localHostUser = remoteHostUser.toLocalHostUser().copy(
                            isLoggedIn = true,
                        )
                        hostDao.insertHostUser(localHostUser)
                        return@withContext SignUpResult.Success
                    }
            }
            SignUpResult.Error(Throwable("User is Logged In"))
        }

    override suspend fun updateColumnsBy(leaderEmail: String, columns: String): Result<Unit> =
        hostRemoteDataSource.updateColumnsBy(leaderEmail, columns)

    override suspend fun updateRowsBy(leaderEmail: String, rows: String): Result<Unit> =
        hostRemoteDataSource.updateRowsBy(leaderEmail, rows)

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
        qrCode,
        isLoggedIn,
        isLeader,
        isOnboardingWelcomeScreenViewed = false,
        rowsOfPlaces,
        columnsOfPlaces,
    )

    private fun LocalHostUser?.toSession(): Session = this?.run {
        Session.LoggedHost(
            Host(
                email,
                photoUrl = "",
                displayName,
                qrCode,
                isLoggedIn,
                isLeader,
                isOnboardingWelcomeScreenViewed,
                rowsOfPlaces,
                columnsOfPlaces,
            )
        )
    } ?: run { Session.HostIsNotLoggedIn }
}