package com.jesusdmedinac.fynd.onboarding.data.model

import android.net.Uri
import com.google.firebase.auth.GetTokenResult

data class HostUser(
    val photoUrl: Uri,
    val displayName: String,
    val email: String,
    val isAnonymous: Boolean,
)