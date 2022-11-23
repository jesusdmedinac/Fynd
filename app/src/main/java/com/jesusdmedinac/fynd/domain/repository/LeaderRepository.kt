package com.jesusdmedinac.fynd.domain.repository

import com.jesusdmedinac.fynd.domain.model.Host

interface LeaderRepository {
    suspend fun joinBy(
        leaderCode: String,
        hostCode: String,
    )
    suspend fun getCurrentLeader(): Host?
    suspend fun isLeader(email: String): Boolean
}