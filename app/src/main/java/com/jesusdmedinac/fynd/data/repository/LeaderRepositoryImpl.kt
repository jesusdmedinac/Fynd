package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.local.HostDao
import com.jesusdmedinac.fynd.data.remote.LeaderRemoteDataSource
import com.jesusdmedinac.fynd.data.repository.mapper.RemoteHostUserToLocalHostUserMapper
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
}