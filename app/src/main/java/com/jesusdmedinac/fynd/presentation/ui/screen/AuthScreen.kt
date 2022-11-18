package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.domain.model.SignInResult
import com.jesusdmedinac.fynd.domain.model.SignUpResult
import com.jesusdmedinac.fynd.domain.model.SignInUserCredentials
import com.jesusdmedinac.fynd.domain.model.SignUpUserCredentials
import com.jesusdmedinac.fynd.domain.usecase.SignInUseCase
import com.jesusdmedinac.fynd.domain.usecase.SignUpUseCase
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignInViewModel
import com.jesusdmedinac.fynd.presentation.viewmodel.AuthSignUpViewModel

@ExperimentalMaterial3Api
@Composable
fun AuthScreen(
    authSignInViewModel: AuthSignInViewModel,
    authSignUpViewModel: AuthSignUpViewModel,
    startDestination: String,
    onWantToSignUpClick: () -> Unit,
    onWantToSignInClick: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavItem.AuthScreen.SignInScreen.baseRoute) {
            val authSignInState by authSignInViewModel.container.stateFlow.collectAsState()

            AuthSignInScreen(
                authSignInState,
                authSignInViewModel,
                onWantToSignUpClick
            )
        }

        composable(NavItem.AuthScreen.SignUpScreen.baseRoute) {
            val authSignUpState by authSignUpViewModel.container.stateFlow.collectAsState()

            AuthSignUpScreen(
                authSignUpState,
                authSignUpViewModel,
                onWantToSignInClick
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun AuthScreenPreview() {
    FyndTheme {
        AuthScreen(
            authSignInViewModel = AuthSignInViewModel(
                signInUseCase = object : SignInUseCase {
                    override suspend fun invoke(userCredentials: SignInUserCredentials): SignInResult {
                        TODO("Not yet implemented")
                    }
                }
            ),
            authSignUpViewModel = AuthSignUpViewModel(
                signUpUseCase = object : SignUpUseCase {
                    override suspend fun invoke(userCredentials: SignUpUserCredentials): SignUpResult {
                        TODO("Not yet implemented")
                    }
                }
            ),
            startDestination = "",
            onWantToSignInClick = {},
            onWantToSignUpClick = {},
        )
    }
}