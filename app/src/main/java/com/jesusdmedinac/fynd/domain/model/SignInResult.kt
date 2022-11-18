package com.jesusdmedinac.fynd.domain.model

sealed class SignInResult {
    object Success : SignInResult()
    class Error(val throwable: Throwable) : SignInResult()
    object UserAlreadyLoggedIn : SignInResult()
    object UserDoesNotExists : SignInResult()
    object WrongPassword : SignInResult()
}