package com.jesusdmedinac.fynd.data.remote.model

data class HostUser(
    val email: String,
    val photoUrl: String,
    val displayName: String,
    val qrCode: String,
    val isLoggedIn: Boolean,
    val isLeader: Boolean,
    val rowsOfPlaces: String,
    val columnsOfPlaces: String,
)