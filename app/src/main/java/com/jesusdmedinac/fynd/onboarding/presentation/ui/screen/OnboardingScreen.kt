package com.jesusdmedinac.fynd.onboarding.presentation.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.onboarding.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.main.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.onboarding.presentation.viewmodel.QRScreenViewModel
import com.jesusdmedinac.fynd.main.presentation.ui.navigation.NavItem as PlacesNavItem

@ExperimentalMaterial3Api
@Composable
fun OnboardingScreen(
    onNavigateToPlacesScreenClick: () -> Unit,
    onNavigateToScanCodeScreenClick: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItem.OnboardingMainScreen.baseRoute) {
        composable(NavItem.OnboardingMainScreen.baseRoute) {
            OnboardingMainScreen {
                navController.navigate(NavItem.QRScreen.baseRoute)
            }
        }

        composable(NavItem.QRScreen.baseRoute) {
            val qrScreenViewModel: QRScreenViewModel = hiltViewModel()

            val qrScreenState by qrScreenViewModel.container.stateFlow.collectAsState()
            val qrScreenSideEffect by qrScreenViewModel
                .container
                .sideEffectFlow
                .collectAsState(initial = QRScreenViewModel.SideEffect.Idle)
            val qrScreenBehavior = qrScreenViewModel.behavior

            LaunchedEffect(qrScreenSideEffect) {
                when (qrScreenSideEffect) {
                    QRScreenViewModel.SideEffect.Idle -> Unit
                    QRScreenViewModel.SideEffect.NavigateToPlacesScreen -> onNavigateToPlacesScreenClick()
                    QRScreenViewModel.SideEffect.NavigateToScanCodeScreen -> onNavigateToScanCodeScreenClick()
                }
            }

            QRScreen(
                qrScreenState,
                qrScreenSideEffect,
                qrScreenBehavior
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun OnboardingScreenPreview() {
    FyndTheme {
        OnboardingScreen(
            onNavigateToPlacesScreenClick = {},
            onNavigateToScanCodeScreenClick = {},
        )
    }
}