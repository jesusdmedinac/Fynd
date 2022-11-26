package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import javax.inject.Inject

class JoinByLeaderCodeUseCase @Inject constructor(
    private val leaderRepository: LeaderRepository,
    private val hostRepository: HostRepository,
) {
    suspend operator fun invoke(leaderCode: String): Result<Unit> = hostRepository.getCurrentHost()
        .map { currentHost ->
            val hostCode = currentHost.qrCode
            return leaderRepository.joinBy(
                leaderCode,
                hostCode,
            )
        }
        .onFailure {
            return Result.failure(it)
        }
}