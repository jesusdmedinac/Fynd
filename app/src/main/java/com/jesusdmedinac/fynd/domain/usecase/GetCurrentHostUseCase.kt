package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

class GetCurrentHostUseCase @Inject constructor(
    private val hostRepository: HostRepository
) {
    suspend operator fun invoke(): Result<Host> = hostRepository.getCurrentHost()
}