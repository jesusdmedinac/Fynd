package com.jesusdmedinac.fynd.main.domain.repository

interface LeaderRepository {
    fun joinBy(
        leaderCode: String,
        hostCode: String,
    )
}