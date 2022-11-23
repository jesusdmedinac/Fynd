package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import javax.inject.Inject

class IsHostALeaderUseCase @Inject constructor(
    private val leaderRepository: LeaderRepository,
) {
    suspend operator fun invoke(email: String): Boolean = leaderRepository.isLeader(email)
}