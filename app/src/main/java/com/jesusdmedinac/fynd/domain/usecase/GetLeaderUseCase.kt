package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import javax.inject.Inject

interface GetLeaderUseCase {
    suspend operator fun invoke(): Host
}

class GetLeaderUseCaseImpl @Inject constructor(
    private val leaderRepository: LeaderRepository,
) : GetLeaderUseCase {
    override suspend fun invoke(): Host = leaderRepository.getCurrentLeader()
        ?: throw Throwable("Current leader is not available")
}