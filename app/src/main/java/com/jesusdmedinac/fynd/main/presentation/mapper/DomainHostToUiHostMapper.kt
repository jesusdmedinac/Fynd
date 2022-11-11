package com.jesusdmedinac.fynd.main.presentation.mapper

import com.jesusdmedinac.fynd.main.presentation.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.onboarding.domain.model.Host
import javax.inject.Inject

class DomainHostToUiHostMapper @Inject constructor() {
    fun map(input: Host): MainScreenViewModel.State.Host = with(input) {
        MainScreenViewModel.State.Host(
            username,
            isLeader,
        )
    }
}