package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import javax.inject.Inject

interface JoinByLeaderCodeUseCase {
    suspend operator fun invoke(leaderCode: String)
}

class JoinByLeaderCodeUseCaseImpl @Inject constructor(
    private val leaderRepository: LeaderRepository,
    private val hostRepository: HostRepository,
) : JoinByLeaderCodeUseCase {
    override suspend fun invoke(leaderCode: String) {
        val currentHost = hostRepository
            .getCurrentHost()
        val hostCode = currentHost
            .qrCode
        leaderRepository.joinBy(
            leaderCode,
            hostCode,
        )
    }
}