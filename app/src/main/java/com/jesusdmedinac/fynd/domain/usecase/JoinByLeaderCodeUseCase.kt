package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import javax.inject.Inject

class JoinByLeaderCodeUseCase @Inject constructor(
    private val leaderRepository: LeaderRepository,
    private val hostRepository: HostRepository,
) {
    suspend operator fun invoke(leaderCode: String) {
        val currentHost = hostRepository.getCurrentHost()
            ?: throw Throwable("Current host is not available for join leader")
        val hostCode = currentHost.qrCode
        leaderRepository.joinBy(
            leaderCode,
            hostCode,
        )
    }
}