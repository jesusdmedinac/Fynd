package com.jesusdmedinac.fynd.onboarding.domain.repository

import com.jesusdmedinac.fynd.onboarding.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface HostRepository {
    suspend fun getCurrentSession(): Flow<Session>
}