package com.jesusdmedinac.fynd.domain.model

sealed class Session {
    object HostIsNotLoggedIn : Session()
    data class LoggedHost(
        val host: Host,
    ) : Session()
}

data class Host(
    val email: String,
    val isLeader: Boolean,
)