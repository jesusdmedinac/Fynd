package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.remote.LeaderRemoteDataSource
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import javax.inject.Inject

class LeaderRepositoryImpl @Inject constructor(
    private val leaderRemoteDataSource: LeaderRemoteDataSource,
) : LeaderRepository {
    override fun joinBy(
        leaderCode: String,
        hostCode: String,
    ) {
        leaderRemoteDataSource.joinBy(leaderCode, hostCode)
    }
}