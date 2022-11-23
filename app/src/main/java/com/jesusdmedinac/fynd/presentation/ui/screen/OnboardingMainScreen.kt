package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.OnboardingHostScreenViewModel
import com.jesusdmedinac.fynd.presentation.viewmodel.OnboardingMainScreenBehavior

@ExperimentalMaterial3Api
@Composable
fun OnboardingMainScreen(
    session: OnboardingHostScreenViewModel.State.Session,
    onboardingHostScreenBehavior: OnboardingMainScreenBehavior,
) {
    val displayName = when (session) {
        is OnboardingHostScreenViewModel.State.Session.HostIsLoggedIn -> session.host.displayName
        else -> "Anónimo"
    }
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hola $displayName, Bienvenido a Fynd",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Una nueva experiencia para anfitriones",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    onboardingHostScreenBehavior.onStartClick()
                }) {
                    Text(text = "Comenzar")
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun OnboardingMainScreenPreview() {
    FyndTheme {
        OnboardingMainScreen(
            session = OnboardingHostScreenViewModel.State.Session.HostIsNotLoggedIn,
            onboardingHostScreenBehavior = object : OnboardingMainScreenBehavior {
                override fun onScreenLoad() {
                    TODO("Not yet implemented")
                }

                override fun onStartClick() {
                    TODO("Not yet implemented")
                }
            },
        )
    }
}