package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        is MainScreenViewModel.State.Session.HostIsLoggedIn -> {
            HomeScreen()
        }
        else -> {
            WelcomeScreen(
                mainScreenBehavior::goToSignInScreen,
                mainScreenBehavior::goToSignUpScreen,
            )
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