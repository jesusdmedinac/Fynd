package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.domain.model.*
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.usecase.SignInUseCase
import com.jesusdmedinac.fynd.domain.usecase.SignUpUseCase
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.screen.authscreen.AuthSignInScreen
import com.jesusdmedinac.fynd.presentation.ui.screen.authscreen.AuthSignUpScreen
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignInViewModel
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignUpViewModel
import kotlinx.coroutines.flow.Flow

@ExperimentalMaterial3Api
@Composable
fun AuthScreen(
    authSignInViewModel: AuthSignInViewModel,
    authSignUpViewModel: AuthSignUpViewModel,
    startDestination: String,
    onWantToSignUpClick: () -> Unit,
    onWantToSignInClick: () -> Unit,
    onUserLoggedIn: () -> Unit,
) {
    val navController = rememberNavController()

    val authSignInSideEffect by authSignInViewModel
        .container
        .sideEffectFlow
        .collectAsState(initial = AuthSignInViewModel.SideEffect.Idle)
    val authSignUpSideEffect by authSignUpViewModel
        .container
        .sideEffectFlow
        .collectAsState(initial = AuthSignUpViewModel.SideEffect.Idle)

    LaunchedEffect(authSignInSideEffect) {
        when (authSignInSideEffect) {
            AuthSignInViewModel.SideEffect.NavigateToOnboarding -> {
                onUserLoggedIn()
            }
            else -> Unit
        }
    }
    LaunchedEffect(authSignUpSideEffect) {
        when (authSignUpSideEffect) {
            AuthSignUpViewModel.SideEffect.NavigateToOnboarding -> {
                onUserLoggedIn()
            }
            else -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavItem.AuthScreen.SignInScreen.baseRoute) {
            val authSignInState by authSignInViewModel.container.stateFlow.collectAsState()

            AuthSignInScreen(
                authSignInState,
                authSignInViewModel,
                onWantToSignUpClick,
            )
        }

        composable(NavItem.AuthScreen.SignUpScreen.baseRoute) {
            val authSignUpState by authSignUpViewModel.container.stateFlow.collectAsState()

            AuthSignUpScreen(
                authSignUpState,
                authSignUpViewModel,
                onWantToSignInClick,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun AuthScreenPreview() {
    val hostRepository = object : HostRepository {
        override suspend fun retrieveCurrentSession(email: String) {
            TODO("Not yet implemented")
        }

        override suspend fun getCurrentSession(): Flow<Session> {
            TODO("Not yet implemented")
        }

        override suspend fun getCurrentHost(): Host? {
            TODO("Not yet implemented")
        }

        override suspend fun signIn(signInUserCredentials: SignInUserCredentials): SignInResult {
            TODO("Not yet implemented")
        }

        override suspend fun signUp(signUpUserCredentials: SignUpUserCredentials): SignUpResult {
            TODO("Not yet implemented")
        }
    }
    FyndTheme {
        AuthScreen(
            authSignInViewModel = AuthSignInViewModel(
                signInUseCase = SignInUseCase(
                    hostRepository = hostRepository
                )
            ),
            authSignUpViewModel = AuthSignUpViewModel(
                signUpUseCase = SignUpUseCase(
                    hostRepository = hostRepository
                )
            ),
            startDestination = "",
            onWantToSignInClick = {},
            onWantToSignUpClick = {},
            onUserLoggedIn = {},
        )
    }
}