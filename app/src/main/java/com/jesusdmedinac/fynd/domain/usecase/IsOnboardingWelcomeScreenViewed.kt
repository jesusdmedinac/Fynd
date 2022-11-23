package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.UserSettingsRepository
import javax.inject.Inject

class IsOnboardingWelcomeScreenViewed @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) {
    suspend operator fun invoke(): Boolean =
        userSettingsRepository.isOnboardingWelcomeScreenViewed()
}