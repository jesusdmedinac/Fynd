package com.jesusdmedinac.fynd.data.remote.mapper

import com.jesusdmedinac.fynd.data.remote.model.SignUpHostUserCredentials
import javax.inject.Inject

class SignUpHostUserCredentialsToMapMapper @Inject constructor(
    private val passwordStringHasher: PasswordStringHasher,
) {
    fun map(input: SignUpHostUserCredentials): Map<String, Any> = with(input) {
        mapOf(
            "photoUrl" to "",
            "displayName" to displayName,
            "email" to email,
            "password" to passwordStringHasher(password),
        )
    }
}