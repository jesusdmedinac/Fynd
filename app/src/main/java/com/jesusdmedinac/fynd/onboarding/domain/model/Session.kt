package com.jesusdmedinac.fynd.onboarding.domain.model

sealed class Session {
    object HostIsNotLoggedIn : Session()
    data class LoggedHost(
        val host: Host,
    ) : Session()
}

data class Host(
    val username: String,
    val isLeader: Boolean,
)