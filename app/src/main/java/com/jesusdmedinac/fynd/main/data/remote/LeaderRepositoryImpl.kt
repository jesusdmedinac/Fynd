package com.jesusdmedinac.fynd.main.data.remote

import com.jesusdmedinac.fynd.main.domain.repository.LeaderRepository
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