package com.jesusdmedinac.fynd.presentation.ui.screen.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.jesusdmedinac.fynd.R
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun WelcomeScreen(
    goToSignInScreen: () -> Unit,
    goToSignUpScreen: () -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "¡Bienvenido a Ancla!",
                style = MaterialTheme.typography.titleLarge,
            )
            Image(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null)
            Button(onClick = {
                goToSignInScreen()
            }) {
                Text(text = "Ya soy voluntario")
            }
            TextButton(onClick = {
                goToSignUpScreen()
            }) {
                Text(text = "¡Quiero ser voluntario!")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun WelcomeScreenPreview() {
    FyndTheme {
        WelcomeScreen(
            goToSignInScreen = {},
            goToSignUpScreen = {}
        )
    }
}