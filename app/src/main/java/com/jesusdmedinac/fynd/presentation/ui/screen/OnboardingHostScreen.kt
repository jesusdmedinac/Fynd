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
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.*

@ExperimentalMaterial3Api
@Composable
fun OnboardingHostScreen(
    onboardingMainScreenState: OnboardingHostScreenViewModel.State = OnboardingHostScreenViewModel.State(),
    qrScreenViewModel: QRScreenViewModel,
    onNavigateToPlacesScreenClick: () -> Unit,
    onNavigateToScanCodeScreenClick: () -> Unit,
) {
    val navController = rememberNavController()
    val session = onboardingMainScreenState.session
    val startDestination = when (session) {
        is OnboardingHostScreenViewModel.State.Session.HostIsLoggedIn -> NavItem.OnboardingHostScreen.Main
        OnboardingHostScreenViewModel.State.Session.HostIsNotLoggedIn -> NavItem.OnboardingHostScreen.QRScreen
    }
    NavHost(
        navController = navController,
        startDestination = startDestination.baseRoute
    ) {
        composable(NavItem.OnboardingHostScreen.Main.baseRoute) {
            OnboardingMainScreen(
                session,
                onStartClick = {
                    navController.navigate(NavItem.OnboardingHostScreen.QRScreen.baseRoute)
                },
            )
        }

        composable(NavItem.OnboardingHostScreen.QRScreen.baseRoute) {
            val qrScreenState by qrScreenViewModel
                .container
                .stateFlow
                .collectAsState()
            val qrScreenSideEffect by qrScreenViewModel
                .container
                .sideEffectFlow
                .collectAsState(initial = QRScreenViewModel.SideEffect.Idle)

            LaunchedEffect(qrScreenSideEffect) {
                when (qrScreenSideEffect) {
                    QRScreenViewModel.SideEffect.Idle -> Unit
                    QRScreenViewModel.SideEffect.NavigateToPlacesScreen -> onNavigateToPlacesScreenClick()
                    QRScreenViewModel.SideEffect.NavigateToScanCodeScreen -> onNavigateToScanCodeScreenClick()
                }
            }

            QRScreen(
                session,
                qrScreenState,
                qrScreenViewModel,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun OnboardingScreenPreview() {
    FyndTheme {
        OnboardingHostScreen(
            onboardingMainScreenState = OnboardingHostScreenViewModel.State(),
            qrScreenViewModel = QRScreenViewModel(),
            onNavigateToPlacesScreenClick = {},
            onNavigateToScanCodeScreenClick = {},
        )
    }
}