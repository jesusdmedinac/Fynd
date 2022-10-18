package com.jesusdmedinac.fynd.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.onboarding.presentation.ui.screen.OnboardingMainScreen
import com.jesusdmedinac.fynd.ui.navigation.NavItem
import com.jesusdmedinac.fynd.ui.screen.MainScreen
import com.jesusdmedinac.fynd.ui.screen.PlacesScreen
import com.jesusdmedinac.fynd.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.viewmodel.MainScreenViewModel
import com.jesusdmedinac.fynd.viewmodel.PlacesScreenViewModel
import com.jesusdmedinac.fynd.onboarding.presentation.ui.navigation.NavItem as OnboardingNavItem

@ExperimentalMaterial3Api
@Composable
fun FyndApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItem.MainScreen.baseRoute) {
        composable(NavItem.MainScreen.baseRoute) {
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

        composable(OnboardingNavItem.OnboardingMainScreen.baseRoute) {
            OnboardingMainScreen()
        }

        composable(NavItem.PlacesNavItem.baseRoute) {
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