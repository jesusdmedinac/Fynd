package com.jesusdmedinac.fynd.domain.repository

interface UserSettingsRepository {
    suspend fun isOnboardingWelcomeScreenViewed(): Boolean
    suspend fun setIsOnboardingWelcomeScreenViewed(isOnboardingWelcomeScreenViewed: Boolean)
}