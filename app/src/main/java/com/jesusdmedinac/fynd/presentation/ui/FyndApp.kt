package com.jesusdmedinac.fynd.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.screen.AuthScreen
import com.jesusdmedinac.fynd.presentation.ui.screen.MainScreen
import com.jesusdmedinac.fynd.presentation.ui.screen.OnboardingScreen
import com.jesusdmedinac.fynd.presentation.ui.screen.PlacesScreen
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.*

@ExperimentalMaterial3Api
@Composable
fun FyndApp(
    launchScanner: () -> Unit,
) {
    val mainScreenViewModel: MainScreenViewModel = hiltViewModel()
    val placesScreenViewModel: PlacesScreenViewModel = hiltViewModel()
    val qrScreenViewModel: QRScreenViewModel = hiltViewModel()
    val authSignInViewModel: AuthSignInViewModel = hiltViewModel()
    val authSignUpViewModel: AuthSignUpViewModel = hiltViewModel()

    val navController = rememberNavController()
    var authStartDestination by remember { mutableStateOf(NavItem.AuthScreen.SignInScreen.baseRoute) }
    NavHost(navController = navController, startDestination = NavItem.MainScreen.baseRoute) {
        composable(NavItem.MainScreen.baseRoute) {
            val mainScreenState by mainScreenViewModel.container.stateFlow.collectAsState()
            val mainScreenSideEffect by mainScreenViewModel
                .container
                .sideEffectFlow
                .collectAsState(initial = MainScreenViewModel.SideEffect.Idle)

            LaunchedEffect(mainScreenSideEffect) {
                when (mainScreenSideEffect) {
                    MainScreenViewModel.SideEffect.NavigateToOnboardingScreen -> {
                        navController.navigate(NavItem.OnboardingMainScreen.Host.baseRoute)
                    }
                    MainScreenViewModel.SideEffect.NavigateToSignInScreen -> {
                        authStartDestination = NavItem.AuthScreen.SignInScreen.baseRoute
                        navController.navigate(NavItem.AuthScreen.Host.baseRoute)
                    }
                    MainScreenViewModel.SideEffect.NavigateToSignUpScreen -> {
                        authStartDestination = NavItem.AuthScreen.SignUpScreen.baseRoute
                        navController.navigate(NavItem.AuthScreen.Host.baseRoute)
                    }
                    MainScreenViewModel.SideEffect.Idle -> Unit
                }
            }

            MainScreen(
                mainScreenState,
                mainScreenViewModel,
            )
        }

        composable(NavItem.PlacesNavItem.baseRoute) {
            val placesScreenState by placesScreenViewModel.container.stateFlow.collectAsState()
            val placesScreenSideEffect by placesScreenViewModel
                .container
                .sideEffectFlow
                .collectAsState(initial = PlacesScreenViewModel.SideEffect.Idle)

            PlacesScreen(
                placesScreenState,
                placesScreenSideEffect,
                placesScreenViewModel,
            )
        }

        composable(NavItem.OnboardingMainScreen.Host.baseRoute) {
            OnboardingScreen(
                qrScreenViewModel = qrScreenViewModel,
                onNavigateToPlacesScreenClick = {
                    navController.navigate(NavItem.PlacesNavItem.baseRoute)
                },
                onNavigateToScanCodeScreenClick = {
                    launchScanner()
                },
            )
        }

        composable(NavItem.AuthScreen.Host.baseRoute) {
            AuthScreen(
                authSignInViewModel,
                authSignUpViewModel,
                authStartDestination,
                onWantToSignUpClick = {
                    authStartDestination = NavItem.AuthScreen.SignUpScreen.baseRoute
                },
                onWantToSignInClick = {
                    authStartDestination = NavItem.AuthScreen.SignInScreen.baseRoute
                },
                onUserLoggedIn = {
                    navController.navigate(NavItem.OnboardingMainScreen.Host.baseRoute)
                },
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun FyndAppPreview() {
    FyndTheme {
        FyndApp(
            launchScanner = {}
        )
    }
}