package com.jesusdmedinac.fynd.domain.repository

interface LeaderRepository {
    fun joinBy(
        leaderCode: String,
        hostCode: String,
    )
}