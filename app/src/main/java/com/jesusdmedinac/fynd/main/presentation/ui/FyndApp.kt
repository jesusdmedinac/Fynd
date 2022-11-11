package com.jesusdmedinac.fynd.main.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.main.presentation.mapper.DomainHostToUiHostMapper
import com.jesusdmedinac.fynd.main.presentation.mapper.DomainSessionToUiSessionMapper
import com.jesusdmedinac.fynd.main.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.main.presentation.ui.screen.MainScreen
import com.jesusdmedinac.fynd.main.presentation.ui.screen.PlacesScreen
import com.jesusdmedinac.fynd.main.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.main.presentation.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.onboarding.domain.model.Session
import com.jesusdmedinac.fynd.onboarding.domain.usecase.HostQrCodeUseCase
import com.jesusdmedinac.fynd.onboarding.domain.usecase.RetrieveCurrentSessionUseCase
import com.jesusdmedinac.fynd.onboarding.presentation.ui.screen.OnboardingScreen
import com.jesusdmedinac.fynd.onboarding.presentation.viewmodel.QRScreenViewModel
import com.jesusdmedinac.fynd.places.presentation.viewmodel.PlacesScreenViewModel
import kotlinx.coroutines.flow.Flow
import com.jesusdmedinac.fynd.onboarding.presentation.ui.navigation.NavItem as OnboardingNavItem

@ExperimentalMaterial3Api
@Composable
fun FyndApp(
    mainScreenViewModel: MainScreenViewModel,
    qrScreenViewModel: QRScreenViewModel,
    placesScreenViewModel: PlacesScreenViewModel,
    launchScanner: () -> Unit,
    launchSignIn: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItem.MainScreen.baseRoute) {
        composable(NavItem.MainScreen.baseRoute) {
            val mainScreenState by mainScreenViewModel.container.stateFlow.collectAsState()
            val mainScreenSideEffect by mainScreenViewModel
                .container
                .sideEffectFlow
                .collectAsState(initial = MainScreenViewModel.SideEffect.Idle)
            val behavior = mainScreenViewModel.behavior

            LaunchedEffect(mainScreenSideEffect) {
                when (mainScreenSideEffect) {
                    MainScreenViewModel.SideEffect.NavigateToOnboardingScreen -> {
                        navController.navigate(OnboardingNavItem.OnboardingMainScreen.baseRoute)
                    }
                    MainScreenViewModel.SideEffect.NavigateToSignInScreen -> {
                        launchSignIn()
                    }
                    MainScreenViewModel.SideEffect.Idle -> Unit
                }
            }

            MainScreen(
                mainScreenState,
                mainScreenSideEffect,
                behavior
            )
        }

        composable(NavItem.PlacesNavItem.baseRoute) {
            val placesScreenState by placesScreenViewModel.container.stateFlow.collectAsState()
            val placesScreenSideEffect by placesScreenViewModel
                .container
                .sideEffectFlow
                .collectAsState(initial = PlacesScreenViewModel.SideEffect.Idle)
            val behavior = placesScreenViewModel.behavior

            PlacesScreen(
                placesScreenState,
                placesScreenSideEffect,
                behavior,
            )
        }

        composable(OnboardingNavItem.OnboardingMainScreen.baseRoute) {
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
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun FyndAppPreview() {
    FyndTheme {
        FyndApp(
            mainScreenViewModel = MainScreenViewModel(
                retrieveCurrentSessionUseCase = object : RetrieveCurrentSessionUseCase {
                    override suspend fun invoke(): Flow<Session> {
                        TODO("Not yet implemented")
                    }
                },
                domainSessionToUiSessionMapper = DomainSessionToUiSessionMapper(
                    domainHostToUiHostMapper = DomainHostToUiHostMapper()
                )
            ),
            qrScreenViewModel = QRScreenViewModel(
                hostQrCodeUseCase = object : HostQrCodeUseCase {
                    override fun invoke(): String {
                        TODO("Not yet implemented")
                    }
                }
            ),
            placesScreenViewModel = PlacesScreenViewModel(),
            launchScanner = {},
            launchSignIn = {},
        )
    }
}