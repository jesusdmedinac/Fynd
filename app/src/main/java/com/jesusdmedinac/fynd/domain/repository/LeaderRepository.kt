package com.jesusdmedinac.fynd.domain.repository

interface LeaderRepository {
    suspend fun joinBy(
        leaderCode: String,
        hostCode: String,
    )
}