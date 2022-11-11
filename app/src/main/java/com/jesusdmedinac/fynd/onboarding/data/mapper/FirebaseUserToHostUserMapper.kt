package com.jesusdmedinac.fynd.onboarding.data.mapper

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.jesusdmedinac.fynd.onboarding.data.model.HostUser
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseUserToHostUserMapper @Inject constructor() {
    fun map(input: FirebaseUser): HostUser = with(input) {
        val photoUrl = photoUrl ?: Uri.EMPTY
        val displayName = displayName ?: ""
        val email = email ?: ""
        val isAnonymous = isAnonymous
        HostUser(
            photoUrl,
            displayName,
            email,
            isAnonymous
        )
    }
}