package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUpdatedHostUseCase @Inject constructor(
    private val hostRepository: HostRepository
) {
    suspend operator fun invoke(): Flow<Result<Host>> = hostRepository.getCurrentHostFlow()
}