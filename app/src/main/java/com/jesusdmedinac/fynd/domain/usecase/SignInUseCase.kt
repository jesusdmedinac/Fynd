package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.SignInResult
import com.jesusdmedinac.fynd.domain.model.SignInUserCredentials
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

interface SignInUseCase {
    suspend operator fun invoke(userCredentials: SignInUserCredentials): SignInResult
}

class SignInUseCaseImpl @Inject constructor(
    private val hostRepository: HostRepository,
) : SignInUseCase {
    override suspend fun invoke(signInUserCredentials: SignInUserCredentials): SignInResult =
        hostRepository.signIn(signInUserCredentials)
}