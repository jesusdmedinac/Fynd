package com.jesusdmedinac.fynd.domain.model

sealed class SignUpResult {
    object Success : SignUpResult()
    class Error(val throwable: Throwable) : SignUpResult()
    object UserAlreadyExists : SignUpResult()
}