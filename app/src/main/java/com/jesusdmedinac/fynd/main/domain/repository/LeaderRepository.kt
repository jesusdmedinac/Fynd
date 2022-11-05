package com.jesusdmedinac.fynd.main.domain.repository

import com.jesusdmedinac.fynd.main.data.remote.LeaderRemoteDataSource
import javax.inject.Inject

interface LeaderRepository {
    fun joinBy(
        leaderCode: String,
        hostCode: String,
    )
}

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