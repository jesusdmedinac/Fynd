package com.jesusdmedinac.fynd.main.presentation.mapper

import com.jesusdmedinac.fynd.main.presentation.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.onboarding.domain.model.Session
import javax.inject.Inject

class DomainSessionToUiSessionMapper @Inject constructor(
    private val domainHostToUiHostMapper: DomainHostToUiHostMapper,
) {
    fun map(input: Session): MainScreenViewModel.State.Session = with(input) {
        when (this) {
            Session.HostIsNotLoggedIn -> MainScreenViewModel.State.Session.HostIsNotLoggedIn
            is Session.LoggedHost -> MainScreenViewModel.State.Session.LoggedHost(
                domainHostToUiHostMapper.map(host)
            )
        }
    }
}