package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    LaunchedEffect(Unit) {
        mainScreenBehavior.getCurrentSession()
    }

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
            override fun getCurrentSession() {
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