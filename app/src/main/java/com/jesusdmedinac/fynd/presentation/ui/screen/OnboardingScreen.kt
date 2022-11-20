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
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.presentation.viewmodel.QRScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun OnboardingScreen(
    mainScreenState: MainScreenViewModel.State = MainScreenViewModel.State(),
    mainScreenBehavior: MainScreenBehavior,
    qrScreenViewModel: QRScreenViewModel,
    onNavigateToPlacesScreenClick: () -> Unit,
    onNavigateToScanCodeScreenClick: () -> Unit,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        mainScreenBehavior.getCurrentSession()
    }

    NavHost(
        navController = navController,
        startDestination = NavItem.OnboardingMainScreen.Main.baseRoute
    ) {
        composable(NavItem.OnboardingMainScreen.Main.baseRoute) {
            OnboardingMainScreen(
                mainScreenState.session
            ) {
                navController.navigate(NavItem.OnboardingMainScreen.QRScreen.baseRoute)
            }
        }

        composable(NavItem.OnboardingMainScreen.QRScreen.baseRoute) {
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
                mainScreenState.session,
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
        OnboardingScreen(
            mainScreenState = MainScreenViewModel.State(),
            mainScreenBehavior = object : MainScreenBehavior {
                override fun getCurrentSession() {
                    TODO("Not yet implemented")
                }

                override fun goToSignInScreen() {
                    TODO("Not yet implemented")
                }

                override fun goToSignUpScreen() {
                    TODO("Not yet implemented")
                }
            },
            qrScreenViewModel = QRScreenViewModel(),
            onNavigateToPlacesScreenClick = {},
            onNavigateToScanCodeScreenClick = {},
        )
    }
}