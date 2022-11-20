package com.jesusdmedinac.fynd.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.domain.usecase.JoinByLeaderCodeUseCase
import com.jesusdmedinac.fynd.domain.usecase.RetrieveCurrentSessionUseCase
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.screen.*
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.*
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterial3Api
@Composable
fun FyndApp(
    launchScanner: () -> Unit,
    mainActivityViewModel: MainActivityViewModel,
) {
    val mainScreenViewModel: MainScreenViewModel = hiltViewModel()
    val placesScreenViewModel: PlacesScreenViewModel = hiltViewModel()
    val qrScreenViewModel: QRScreenViewModel = hiltViewModel()
    val authSignInViewModel: AuthSignInViewModel = hiltViewModel()
    val authSignUpViewModel: AuthSignUpViewModel = hiltViewModel()

    val navController = rememberNavController()
    var authStartDestination by remember { mutableStateOf(NavItem.AuthScreen.SignInScreen.baseRoute) }
    val mainActivitySideEffect by mainActivityViewModel
        .container
        .sideEffectFlow
        .collectAsState(initial = MainActivityViewModel.SideEffect.Idle)

    LaunchedEffect(mainActivitySideEffect) {
        when (mainActivitySideEffect) {
            MainActivityViewModel.SideEffect.Idle -> Unit
            MainActivityViewModel.SideEffect.NavigateToHome -> {
                navController.navigate(NavItem.HomeNavItem.Host.baseRoute)
            }
        }
    }

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
            val mainScreenState by mainScreenViewModel.container.stateFlow.collectAsState()
            OnboardingScreen(
                mainScreenState,
                mainScreenViewModel,
                qrScreenViewModel,
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
                    mainActivityViewModel.retrieveCurrentSession()
                    navController.navigate(NavItem.OnboardingMainScreen.Host.baseRoute)
                },
            )
        }

        composable(NavItem.HomeNavItem.Host.baseRoute) {
            HomeScreen()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun FyndAppPreview() {
    FyndTheme {
        FyndApp(
            launchScanner = {},
            mainActivityViewModel = MainActivityViewModel(
                joinByLeaderCodeUseCase = object : JoinByLeaderCodeUseCase {
                    override suspend fun invoke(leaderCode: String) {
                        TODO("Not yet implemented")
                    }
                },
                retrieveCurrentSessionUseCase = object : RetrieveCurrentSessionUseCase {
                    override suspend fun invoke(viewModelScope: CoroutineScope) {
                        TODO("Not yet implemented")
                    }
                }
            ),
        )
    }
}