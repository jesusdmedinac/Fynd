package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.model.SignInResult
import com.jesusdmedinac.fynd.domain.model.SignInUserCredentials
import com.jesusdmedinac.fynd.domain.usecase.SignInUseCase
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.*
import javax.inject.Inject

@HiltViewModel
class AuthSignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) :
    ViewModel(),
    AuthSignInBehavior,
    ContainerHost<AuthSignInViewModel.State, AuthSignInViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun onEmailChange(email: String) {
        intent {
            reduce { state.copy(email = email) }

            validateForm()
        }
    }

    override fun onPasswordChange(password: String) {
        intent {
            reduce {
                state.copy(
                    password = password,
                )
            }

            validateForm()
        }
    }

    private fun State.isPasswordError(): Boolean =
        if (confirmPassword.isEmpty()) false
        else password != confirmPassword

    override fun onPasswordVisibilityToggle() {
        intent {
            reduce { state.copy(isPasswordVisible = !state.isPasswordVisible) }
        }
    }

    override fun onLoginClick() {
        intent {
            validateForm()
            if (state.formIsNotValid())
                return@intent

            reduce { state.copy(isLoading = true) }
            when (val signInResult = signInUseCase(state.getSignInUserCredentials())) {
                is SignInResult.Error -> {
                    reduce {
                        state.copy(
                            isLoading = false,
                            signInErrorMessage = signInResult.throwable.message.toString()
                        )
                    }
                }
                SignInResult.Success,
                SignInResult.UserAlreadyLoggedIn -> {
                    reduce {
                        state.copy(
                            isEmailError = false,
                            isPasswordVisible = false,
                            isLoading = false,
                        )
                    }

                    postSideEffect(SideEffect.NavigateToOnboarding)
                }
                SignInResult.UserDoesNotExists -> {
                    reduce {
                        state.copy(
                            isEmailError = true,
                            isLoading = false,
                        )
                    }
                }
                SignInResult.WrongPassword -> {
                    reduce {
                        state.copy(
                            isPasswordError = true,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private suspend fun SimpleSyntax<State, SideEffect>.validateForm() {
        reduce {
            state.copy(
                isEmailError = state.isNotValidEmail(),
                signInErrorMessage = "",
                isPasswordError = state.isPasswordError()
            )
        }
    }

    private fun State.isNotValidEmail(): Boolean =
        email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex())

    private fun State.formIsNotValid(): Boolean = isEmailError || isPasswordError

    private fun State.getSignInUserCredentials(): SignInUserCredentials =
        SignInUserCredentials(email, password)

    data class State(
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val signInErrorMessage: String = "",
        val isEmailError: Boolean = false,
        val isPasswordError: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false,
        val startDestination: String = NavItem.AuthScreen.SignInScreen.baseRoute
    )

    sealed class SideEffect {
        object Idle : SideEffect()
        object NavigateToOnboarding : SideEffect()
    }
}

interface AuthSignInBehavior {
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onPasswordVisibilityToggle()
    fun onLoginClick()
}