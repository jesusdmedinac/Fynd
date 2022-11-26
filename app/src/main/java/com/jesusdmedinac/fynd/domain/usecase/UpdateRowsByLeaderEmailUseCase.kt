package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

class UpdateRowsByLeaderEmailUseCase @Inject constructor(
    private val currentHostUseCase: GetCurrentHostUseCase,
    private val hostRepository: HostRepository,
) {
    suspend operator fun invoke(rows: String): Result<Unit> = currentHostUseCase()
        .map { it.email }
        .map { email -> hostRepository.updateRowsBy(email, rows) }
}