package com.jesusdmedinac.fynd.onboarding.domain.usecase

import com.jesusdmedinac.fynd.onboarding.domain.model.Session
import com.jesusdmedinac.fynd.onboarding.domain.repository.HostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveCurrentSessionUseCase {
    suspend operator fun invoke(): Flow<Session>
}

class RetrieveCurrentSessionUseCaseImpl @Inject constructor(
    private val hostRepository: HostRepository
) : RetrieveCurrentSessionUseCase {
    override suspend fun invoke(): Flow<Session> = hostRepository.getCurrentSession()
}