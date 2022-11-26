package com.jesusdmedinac.fynd.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.domain.model.*
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import com.jesusdmedinac.fynd.domain.usecase.GetCurrentHostUseCase
import com.jesusdmedinac.fynd.domain.usecase.JoinByLeaderCodeUseCase
import com.jesusdmedinac.fynd.domain.usecase.RetrieveCurrentSessionUseCase
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.screen.*
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.*
import kotlinx.coroutines.flow.Flow

@ExperimentalFoundationApi
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
    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
    val onboardingHostScreenViewModel: OnboardingHostScreenViewModel = hiltViewModel()

    val navController = rememberNavController()
    var authStartDestination by remember { mutableStateOf(NavItem.AuthScreen.SignInScreen.baseRoute) }
    val mainActivitySideEffect by mainActivityViewModel
        .container
        .sideEffectFlow
        .collectAsState(initial = MainActivityViewModel.SideEffect.Idle)

    LaunchedEffect(Unit) {
        mainActivityViewModel.retrieveCurrentSession()
    }

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
                        navController.navigate(NavItem.OnboardingHostScreen.Host.baseRoute)
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

        composable(NavItem.OnboardingHostScreen.Host.baseRoute) {
            OnboardingHostScreen(
                onboardingHostScreenViewModel,
                qrScreenViewModel,
                onNavigateToPlacesScreenClick = {
                    navController.navigate(NavItem.HomeNavItem.Host.baseRoute)
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
                    navController.navigate(NavItem.OnboardingHostScreen.Host.baseRoute)
                },
            )
        }

        composable(NavItem.HomeNavItem.Host.baseRoute) {
            val homeState by homeScreenViewModel.container.stateFlow.collectAsState()
            val homeSideEffect by homeScreenViewModel.container.sideEffectFlow.collectAsState(initial = HomeScreenViewModel.SideEffect.Idle)

            HomeHostScreen(
                homeState,
                homeSideEffect,
                homeScreenViewModel,
                placesScreenViewModel,
            )
        }
    }
}