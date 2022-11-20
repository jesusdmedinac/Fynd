package com.jesusdmedinac.fynd.data.remote.mapper

import com.jesusdmedinac.fynd.data.remote.model.HostUser
import javax.inject.Inject

class MapToHostUserMapper @Inject constructor() {
    fun map(input: Map<String, Any>) = with(input) {
        val displayName: String = this["displayName"] as? String ?: ""
        val email: String = this["email"] as? String ?: ""
        val qrCode: String = this["qrCode"] as? String ?: ""
        val isLoggedIn: Boolean = this["isLoggedIn"] as? Boolean ?: false
        val isLeader: Boolean = this["isLeader"] as? Boolean ?: false
        HostUser(
            email,
            photoUrl = "",
            displayName,
            qrCode,
            isLoggedIn,
            isLeader,
        )
    }
}