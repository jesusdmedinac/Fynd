package com.jesusdmedinac.fynd.data.repository.mapper

import javax.inject.Inject
import com.jesusdmedinac.fynd.data.local.model.HostUser as LocalHostUser
import com.jesusdmedinac.fynd.data.remote.model.HostUser as RemoteHostUser

class RemoteHostUserToLocalHostUserMapper @Inject constructor() {
    fun map(input: RemoteHostUser): LocalHostUser = with(input) {
        LocalHostUser(
            email,
            photoUrl,
            displayName,
            qrCode,
            isLoggedIn,
            isLeader,
            isOnboardingWelcomeScreenViewed = false,
            rowsOfPlaces,
            columnsOfPlaces,
        )
    }
}