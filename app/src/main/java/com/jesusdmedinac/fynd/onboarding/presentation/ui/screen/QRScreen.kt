package com.jesusdmedinac.fynd.onboarding.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jesusdmedinac.fynd.main.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.onboarding.presentation.ui.screen.qrscreen.QRDrawer
import com.jesusdmedinac.fynd.onboarding.presentation.viewmodel.QRScreenViewModel
import kotlin.random.Random

@ExperimentalMaterial3Api
@Composable
fun QRScreen(
    qrScreenState: QRScreenViewModel.State = QRScreenViewModel.State(),
    qrScreenSideEffect: QRScreenViewModel.SideEffect = QRScreenViewModel.SideEffect.Idle,
    qrScreenBehavior: QRScreenViewModel.Behavior
) {
    qrScreenBehavior.generateNewCode()
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val qrCode = qrScreenState.qrCode
            QRDrawer(
                qrCode,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .background(Color.Red)
            )
            Text(text = "Comparte tu código de líder")
            Button(onClick = { /*TODO*/ }) {
                Text(text = "O escanea el código del líder")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun QRScreenPreview() {
    FyndTheme {
        QRScreen(
            qrScreenBehavior = object : QRScreenViewModel.Behavior {
                override fun generateNewCode() {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}