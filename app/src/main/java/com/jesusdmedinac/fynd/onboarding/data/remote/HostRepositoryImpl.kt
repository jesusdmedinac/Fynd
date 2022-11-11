package com.jesusdmedinac.fynd.onboarding.data.remote

import com.jesusdmedinac.fynd.onboarding.data.model.HostUser
import com.jesusdmedinac.fynd.onboarding.domain.model.Host
import com.jesusdmedinac.fynd.onboarding.domain.model.Session
import com.jesusdmedinac.fynd.onboarding.domain.repository.HostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class HostRepositoryImpl @Inject constructor(
    private val hostRemoteDataSource: HostRemoteDataSource,
    @Named("io-dispatcher")
    private val ioDispatcher: CoroutineDispatcher,
) : HostRepository {
    override suspend fun getCurrentSession(): Flow<Session> =
        hostRemoteDataSource.getCurrentSession()
            .flowOn(ioDispatcher)
            .map { it.toSession() }

    private fun HostUser?.toSession(): Session = this
        ?.let { Session.LoggedHost(Host(it.email, isLeader = false)) }
        ?: run { Session.HostIsNotLoggedIn }
}