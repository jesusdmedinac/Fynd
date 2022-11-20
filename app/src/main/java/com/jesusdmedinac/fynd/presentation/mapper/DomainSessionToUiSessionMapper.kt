package com.jesusdmedinac.fynd.presentation.mapper

import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.domain.model.Session
import javax.inject.Inject

class DomainSessionToUiSessionMapper @Inject constructor(
    private val domainHostToUiHostMapper: DomainHostToUiHostMapper,
) {
    fun map(input: Session): MainScreenViewModel.State.Session = when (input) {
        Session.HostIsNotLoggedIn -> MainScreenViewModel.State.Session.HostIsNotLoggedIn
        is Session.LoggedHost -> MainScreenViewModel.State.Session.HostIsLoggedIn(
            domainHostToUiHostMapper.map(input.host)
        )
    }
}