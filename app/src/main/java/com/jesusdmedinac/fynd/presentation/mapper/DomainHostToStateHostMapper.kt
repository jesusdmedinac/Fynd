package com.jesusdmedinac.fynd.presentation.mapper

import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenViewModel
import javax.inject.Inject

class DomainHostToStateHostMapper @Inject constructor() {
    fun map(input: Host): MainScreenViewModel.State.Host = with(input) {
        MainScreenViewModel.State.Host(email, displayName, qrCode, isLeader)
    }
}