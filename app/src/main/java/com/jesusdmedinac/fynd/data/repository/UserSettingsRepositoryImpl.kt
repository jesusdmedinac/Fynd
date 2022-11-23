package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.local.HostDao
import com.jesusdmedinac.fynd.domain.repository.UserSettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UserSettingsRepositoryImpl @Inject constructor(
    private val hostDao: HostDao,
    @Named("io-dispatcher")
    private val ioDispatcher: CoroutineDispatcher,
) : UserSettingsRepository {
    override suspend fun isOnboardingWelcomeScreenViewed(): Boolean = withContext(ioDispatcher) {
        hostDao.isOnboardingWelcomeScreenViewed()
    }

    override suspend fun setIsOnboardingWelcomeScreenViewed(isOnboardingWelcomeScreenViewed: Boolean) {
        withContext(ioDispatcher) {
            hostDao.setIsOnboardingWelcomeScreenViewed(isOnboardingWelcomeScreenViewed)
        }
    }
}