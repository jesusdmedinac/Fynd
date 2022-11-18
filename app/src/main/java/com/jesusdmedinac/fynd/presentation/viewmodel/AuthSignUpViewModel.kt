package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.model.SignUpResult
import com.jesusdmedinac.fynd.domain.model.SignUpUserCredentials
import com.jesusdmedinac.fynd.domain.usecase.SignUpUseCase
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.*
import javax.inject.Inject

@HiltViewModel
class AuthSignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) :
    ViewModel(),
    AuthSignUpBehavior,
    ContainerHost<AuthSignUpViewModel.State, AuthSignUpViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun onEmailChange(email: String) {
        intent {
            reduce { state.copy(email = email) }

            validateForm()
        }
    }

    override fun onDisplayNameChange(displayName: String) {
        intent {
            reduce { state.copy(displayName = displayName) }

            reduce { state.copy(isDisplayNameError = state.displayName.isEmpty()) }
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

    override fun onConfirmPasswordChange(confirmPassword: String) {
        intent {
            reduce {
                state.copy(
                    confirmPassword = confirmPassword,
                )
            }
            validateForm()
        }
    }

    override fun onPasswordVisibilityToggle() {
        intent {
            reduce { state.copy(isPasswordVisible = !state.isPasswordVisible) }
        }
    }

    override fun onSignUpClick() {
        intent {
            validateForm()
            if (state.isNotValidForm())
                return@intent

            reduce { state.copy(isLoading = true) }
            when (val signUpResult = signUpUseCase(state.getSignUpUserCredentials())) {
                is SignUpResult.Error -> reduce {
                    state.copy(
                        isLoading = false,
                        signUpErrorMessage = signUpResult.throwable.message.toString()
                    )
                }
                SignUpResult.Success -> {
                    reduce {
                        state.copy(
                            isEmailError = false,
                            isPasswordVisible = false,
                            isLoading = false,
                        )
                    }

                    postSideEffect(SideEffect.NavigateToOnboarding)
                }
                SignUpResult.UserAlreadyExists -> {
                    reduce {
                        state.copy(
                            isEmailError = true,
                            isPasswordVisible = false,
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
                isPasswordError = state.isPasswordError()
            )
        }
    }

    private fun State.isNotValidEmail(): Boolean =
        email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex())

    private fun State.isNotValidForm(): Boolean =
        isEmailError || isDisplayNameError || isPasswordError

    private fun State.isPasswordError(): Boolean =
        if (confirmPassword.isEmpty()) false
        else password != confirmPassword

    private fun State.getSignUpUserCredentials(): SignUpUserCredentials =
        SignUpUserCredentials(email, displayName, password)

    data class State(
        val email: String = "",
        val displayName: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val signUpErrorMessage: String = "",
        val isEmailError: Boolean = false,
        val isDisplayNameError: Boolean = false,
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

interface AuthSignUpBehavior {
    fun onEmailChange(email: String)
    fun onDisplayNameChange(displayName: String)
    fun onPasswordChange(password: String)
    fun onConfirmPasswordChange(confirmPassword: String)
    fun onPasswordVisibilityToggle()
    fun onSignUpClick()
}