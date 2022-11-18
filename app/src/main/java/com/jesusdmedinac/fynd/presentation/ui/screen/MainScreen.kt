package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    mainScreenState: MainScreenViewModel.State = MainScreenViewModel.State(),
    mainScreenBehavior: MainScreenBehavior
) {
    mainScreenBehavior.goToOnboardingScreen()

    when (mainScreenState.session) {
        MainScreenViewModel.State.Session.HostIsNotLoggedIn -> {
            WelcomeScreen(
                mainScreenBehavior::goToSignInScreen,
                mainScreenBehavior::goToSignUpScreen,
            )
        }
        is MainScreenViewModel.State.Session.HostIsLoggedIn -> {
            HomeScreen()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun MainScreenPreview() {
    FyndTheme {
        MainScreen(mainScreenBehavior = object : MainScreenBehavior {
            override fun goToOnboardingScreen() {
                TODO("Not yet implemented")
            }

            override fun goToSignInScreen() {
                TODO("Not yet implemented")
            }

            override fun goToSignUpScreen() {
                TODO("Not yet implemented")
            }
        })
    }
}