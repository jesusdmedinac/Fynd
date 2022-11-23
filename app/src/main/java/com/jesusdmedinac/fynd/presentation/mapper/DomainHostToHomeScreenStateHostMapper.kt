package com.jesusdmedinac.fynd.presentation.mapper

import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.presentation.viewmodel.HomeScreenViewModel
import javax.inject.Inject

class DomainHostToHomeScreenStateHostMapper @Inject constructor() {
    fun map(input: Host): HomeScreenViewModel.State.Host = with(input) {
        HomeScreenViewModel.State.Host(
            email,
            displayName,
            qrCode,
            isLeader,
            isOnboardingWelcomeScreenViewed,
        )
    }
}