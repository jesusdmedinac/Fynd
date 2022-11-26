package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

class UpdateColumnsByLeaderEmailUseCase @Inject constructor(
    private val currentHostUseCase: GetCurrentHostUseCase,
    private val hostRepository: HostRepository,
) {
    suspend operator fun invoke(columns: String): Result<Unit> = currentHostUseCase()
        .map { it.email }
        .map { email -> hostRepository.updateColumnsBy(email, columns) }
}