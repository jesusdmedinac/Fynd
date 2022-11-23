package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.SignUpResult
import com.jesusdmedinac.fynd.domain.model.SignUpUserCredentials
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val hostRepository: HostRepository,
) {
    suspend operator fun invoke(signUpUserCredentials: SignUpUserCredentials): SignUpResult =
        hostRepository.signUp(signUpUserCredentials)
}