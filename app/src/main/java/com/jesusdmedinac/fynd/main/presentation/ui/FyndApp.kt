package com.jesusdmedinac.fynd.main.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.main.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.main.presentation.ui.screen.MainScreen
import com.jesusdmedinac.fynd.main.presentation.ui.screen.PlacesScreen
import com.jesusdmedinac.fynd.main.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.main.presentation.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.onboarding.presentation.ui.screen.OnboardingScreen
import com.jesusdmedinac.fynd.places.presentation.viewmodel.PlacesScreenViewModel
import com.jesusdmedinac.fynd.onboarding.presentation.ui.navigation.NavItem as OnboardingNavItem

@ExperimentalMaterial3Api
@Composable
fun FyndApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItem.MainScreen.baseRoute) {
        listOf(
            NavItem.MainScreen,
            NavItem.PlacesNavItem,
            NavItem.ScanCodeNavItem,
        ).forEach {
            when (it) {
                NavItem.MainScreen -> composable(it.baseRoute) {
                    val mainScreenViewModel: MainScreenViewModel = hiltViewModel()

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
                            else -> Unit
                        }
                    }

                    MainScreen(
                        mainScreenState,
                        mainScreenSideEffect,
                        behavior
                    )
                }
                NavItem.PlacesNavItem -> composable(it.baseRoute) {
                    val placesScreenViewModel: PlacesScreenViewModel = hiltViewModel()

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
                NavItem.ScanCodeNavItem -> composable(it.baseRoute) {

                }
            }
        }

        composable(OnboardingNavItem.OnboardingMainScreen.baseRoute) {
            OnboardingScreen(
                onNavigateToPlacesScreenClick = {
                    navController.navigate(NavItem.PlacesNavItem.baseRoute)
                },
                onNavigateToScanCodeScreenClick = {
                    navController.navigate(NavItem.ScanCodeNavItem.baseRoute)
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
        FyndApp()
    }
}