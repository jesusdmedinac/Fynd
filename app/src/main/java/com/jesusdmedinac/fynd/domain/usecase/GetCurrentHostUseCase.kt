package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

interface GetCurrentHostUseCase {
    suspend operator fun invoke(): Host
}

class GetCurrentHostUseCaseImpl @Inject constructor(
    private val hostRepository: HostRepository
) : GetCurrentHostUseCase {
    override suspend fun invoke(): Host = hostRepository.getCurrentHost()
        ?: throw Throwable("Current host is not available")
}