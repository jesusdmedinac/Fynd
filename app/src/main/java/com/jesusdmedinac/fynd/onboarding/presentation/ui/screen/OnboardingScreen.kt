package com.jesusdmedinac.fynd.onboarding.presentation.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.onboarding.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun OnboardingMainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavItem.OnboardingMainScreen.baseRoute) {
        composable(NavItem.OnboardingMainScreen.baseRoute) {
            OnboardingMainScreen {
                navController.navigate(NavItem.QRScreen.baseRoute)
            }
        }

        composable(NavItem.QRScreen.baseRoute) {
            QRScreen()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun OnboardingScreenPreview() {
    FyndTheme {
        OnboardingMainScreen()
    }
}