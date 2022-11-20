package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.local.HostDao
import com.jesusdmedinac.fynd.data.remote.LeaderRemoteDataSource
import com.jesusdmedinac.fynd.data.repository.mapper.RemoteHostUserToLocalHostUserMapper
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import javax.inject.Inject

class LeaderRepositoryImpl @Inject constructor(
    private val leaderRemoteDataSource: LeaderRemoteDataSource,
    private val hostDao: HostDao,
    private val remoteHostUserToLocalHostUserMapper: RemoteHostUserToLocalHostUserMapper,
) : LeaderRepository {
    override suspend fun joinBy(
        leaderCode: String,
        hostCode: String,
    ) {
        val remoteHostUser = leaderRemoteDataSource.joinBy(leaderCode, hostCode)
        val localHostUser = remoteHostUserToLocalHostUserMapper.map(remoteHostUser)
        hostDao.insertHostUser(localHostUser)
    }
}