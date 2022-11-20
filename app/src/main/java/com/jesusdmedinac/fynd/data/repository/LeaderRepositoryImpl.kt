package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.local.HostDao
import com.jesusdmedinac.fynd.data.local.model.HostUser
import com.jesusdmedinac.fynd.data.remote.LeaderRemoteDataSource
import com.jesusdmedinac.fynd.data.repository.mapper.RemoteHostUserToLocalHostUserMapper
import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.domain.model.Session
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LeaderRepositoryImpl @Inject constructor(
    private val leaderRemoteDataSource: LeaderRemoteDataSource,
    private val hostDao: HostDao,
    private val remoteHostUserToLocalHostUserMapper: RemoteHostUserToLocalHostUserMapper,
    @Named("io-dispatcher")
    private val ioDispatcher: CoroutineDispatcher,
) : LeaderRepository {
    override suspend fun joinBy(
        leaderCode: String,
        hostCode: String,
    ) {
        withContext(ioDispatcher) {
            val remoteHostUser = leaderRemoteDataSource.joinBy(leaderCode, hostCode)
            val localHostUser = remoteHostUserToLocalHostUserMapper
                .map(remoteHostUser)
                .copy(isLeader = true)
            hostDao.insertHostUser(localHostUser)
        }
    }

    override suspend fun getCurrentLeader(): Host? = withContext(ioDispatcher) {
        (hostDao.leaderHostUser()
            ?.toSession() as? Session.LoggedHost)
            ?.host
    }

    private fun HostUser?.toSession(): Session = this?.run {
        Session.LoggedHost(
            Host(
                email,
                photoUrl = "",
                displayName,
                qrCode,
                isLoggedIn,
                isLeader,
            )
        )
    } ?: run { Session.HostIsNotLoggedIn }
}