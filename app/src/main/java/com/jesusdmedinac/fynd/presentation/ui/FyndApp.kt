package com.jesusdmedinac.fynd.presentation.ui

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

        composable(NavItem.OnboardingHostScreen.Host.baseRoute) {
            val onboardingMainScreenState by onboardingHostScreenViewModel.container.stateFlow.collectAsState()

            LaunchedEffect(Unit) {
                mainScreenViewModel.getCurrentSession()
            }

            OnboardingHostScreen(
                onboardingMainScreenState,
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
                    mainActivityViewModel.retrieveCurrentSession()
                    navController.navigate(NavItem.OnboardingHostScreen.Host.baseRoute)
                },
            )
        }

        composable(NavItem.HomeNavItem.Host.baseRoute) {
            val homeState by homeScreenViewModel.container.stateFlow.collectAsState()
            val homeSideEffect by homeScreenViewModel.container.sideEffectFlow.collectAsState(initial = HomeScreenViewModel.SideEffect.Idle)

            HomeScreen(
                homeState,
                homeSideEffect,
                homeScreenViewModel
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun FyndAppPreview() {
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
        FyndApp(
            launchScanner = {},
            mainActivityViewModel = MainActivityViewModel(
                joinByLeaderCodeUseCase = JoinByLeaderCodeUseCase(
                    leaderRepository = object : LeaderRepository {
                        override suspend fun joinBy(leaderCode: String, hostCode: String) {
                            TODO("Not yet implemented")
                        }

                        override suspend fun getCurrentLeader(): Host? {
                            TODO("Not yet implemented")
                        }
                    },
                    hostRepository = hostRepository
                ),
                retrieveCurrentSessionUseCase = RetrieveCurrentSessionUseCase(
                    hostRepository = hostRepository,
                    getCurrentHostUseCase = GetCurrentHostUseCase(
                        hostRepository = hostRepository
                    )
                ),
            ),
        )
    }
}