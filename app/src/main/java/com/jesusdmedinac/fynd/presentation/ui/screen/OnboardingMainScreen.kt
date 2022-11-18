package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun OnboardingMainScreen(
    onStartClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "Bienvenido a Fynd", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Una nueva experiencia para anfitriones", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onStartClick) {
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
        OnboardingMainScreen() {}
    }
}