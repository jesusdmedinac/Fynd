package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.SignUpResult
import com.jesusdmedinac.fynd.domain.model.SignInUserCredentials
import com.jesusdmedinac.fynd.domain.model.SignUpUserCredentials
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

interface SignUpUseCase {
    suspend operator fun invoke(signUpUserCredentials: SignUpUserCredentials): SignUpResult
}

class SignUpUseCaseImpl @Inject constructor(
    private val hostRepository: HostRepository,
) : SignUpUseCase {
    override suspend fun invoke(signUpUserCredentials: SignUpUserCredentials): SignUpResult =
        hostRepository.signUp(signUpUserCredentials)
}