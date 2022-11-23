package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SetOnboardingWelcomeScreenViewed @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) {
    suspend operator fun invoke(isOnboardingWelcomeScreenViewed: Boolean) {
        userSettingsRepository.setIsOnboardingWelcomeScreenViewed(isOnboardingWelcomeScreenViewed)
    }
}