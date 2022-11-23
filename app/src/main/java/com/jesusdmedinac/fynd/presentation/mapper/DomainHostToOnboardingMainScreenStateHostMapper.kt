package com.jesusdmedinac.fynd.presentation.mapper

import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.presentation.viewmodel.OnboardingHostScreenViewModel
import javax.inject.Inject

class DomainHostToOnboardingMainScreenStateHostMapper @Inject constructor() {
    fun map(input: Host): OnboardingHostScreenViewModel.State.Host = with(input) {
        OnboardingHostScreenViewModel.State.Host(
            email,
            displayName,
            qrCode,
            isLeader,
            isOnboardingWelcomeScreenViewed,
        )
    }
}