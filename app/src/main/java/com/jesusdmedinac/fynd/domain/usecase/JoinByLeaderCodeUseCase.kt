package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import com.jesusdmedinac.fynd.domain.usecase.HostQrCodeUseCase
import javax.inject.Inject

interface JoinByLeaderCodeUseCase {
    operator fun invoke(leaderCode: String)
}

class JoinByLeaderCodeUseCaseImpl @Inject constructor(
    private val leaderRepository: LeaderRepository,
    private val hostQrCodeUseCase: HostQrCodeUseCase,
) : JoinByLeaderCodeUseCase {
    override fun invoke(leaderCode: String) {
        val hostCode = hostQrCodeUseCase()
        leaderRepository.joinBy(
            leaderCode,
            hostCode,
        )
    }
}