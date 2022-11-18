package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.presentation.ui.screen.qrscreen.QRDrawer
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.QRScreenBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.QRScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun QRScreen(
    qrScreenState: QRScreenViewModel.State = QRScreenViewModel.State(),
    qrScreenSideEffect: QRScreenViewModel.SideEffect = QRScreenViewModel.SideEffect.Idle,
    qrScreenBehavior: QRScreenBehavior
) {
    qrScreenBehavior.generateNewCode()
    Scaffold(
        modifier = Modifier.padding(32.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(onClick = qrScreenBehavior::onStartServingClick) {
                Text(text = "¡Comenzar a servir!")
            }
            val qrCode = qrScreenState.qrCode
            QRDrawer(
                qrCode,
                modifier = Modifier
                    .size(512.dp)
            )
            Text(text = qrCode, style = MaterialTheme.typography.titleLarge)
            Text(text = "Comparte tu código de líder")
            Button(onClick = qrScreenBehavior::onScanCodeClick) {
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
            qrScreenBehavior = object : QRScreenBehavior {
                override fun generateNewCode() {
                    TODO("Not yet implemented")
                }

                override fun onStartServingClick() {
                    TODO("Not yet implemented")
                }

                override fun onScanCodeClick() {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}