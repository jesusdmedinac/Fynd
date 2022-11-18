package com.jesusdmedinac.fynd.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.jesusdmedinac.fynd.data.remote.model.HostUser
import javax.inject.Inject

class FirebaseUserToHostUserMapper @Inject constructor() {
    fun map(input: FirebaseUser): HostUser = with(input) {
        val photoUrl = photoUrl?.path ?: ""
        val displayName = displayName ?: ""
        val email = email ?: ""
        HostUser(
            photoUrl,
            displayName,
            email,
        )
    }
}