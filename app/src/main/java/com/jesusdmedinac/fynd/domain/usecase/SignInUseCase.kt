package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.SignInResult
import com.jesusdmedinac.fynd.domain.model.SignInUserCredentials
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val hostRepository: HostRepository,
) {
    suspend operator fun invoke(signInUserCredentials: SignInUserCredentials): SignInResult =
        hostRepository.signIn(signInUserCredentials)
}