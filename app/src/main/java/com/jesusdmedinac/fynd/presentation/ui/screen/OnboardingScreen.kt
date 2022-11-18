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
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.domain.usecase.HostQrCodeUseCase
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.viewmodel.QRScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun OnboardingScreen(
    qrScreenViewModel: QRScreenViewModel,
    onNavigateToPlacesScreenClick: () -> Unit,
    onNavigateToScanCodeScreenClick: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavItem.OnboardingMainScreen.Main.baseRoute
    ) {
        composable(NavItem.OnboardingMainScreen.Main.baseRoute) {
            OnboardingMainScreen {
                navController.navigate(NavItem.OnboardingMainScreen.QRScreen.baseRoute)
            }
        }

        composable(NavItem.OnboardingMainScreen.QRScreen.baseRoute) {
            val qrScreenState by qrScreenViewModel.container.stateFlow.collectAsState()
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
                qrScreenState,
                qrScreenSideEffect,
                qrScreenViewModel
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
            qrScreenViewModel = QRScreenViewModel(
                hostQrCodeUseCase = object : HostQrCodeUseCase {
                    override fun invoke(): String {
                        TODO("Not yet implemented")
                    }
                }
            ),
            onNavigateToPlacesScreenClick = {},
            onNavigateToScanCodeScreenClick = {},
        )
    }
}