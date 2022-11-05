package com.jesusdmedinac.fynd.main.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jesusdmedinac.fynd.main.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.main.presentation.viewmodel.MainScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    mainScreenState: MainScreenViewModel.State = MainScreenViewModel.State(),
    mainScreenSideEffect: MainScreenViewModel.SideEffect = MainScreenViewModel.SideEffect.Idle,
    behavior: MainScreenViewModel.Behavior
) {
    behavior.goToOnboardingScreen()

    if (mainScreenState.isUserLoggedIn) {
        Scaffold(
            bottomBar = {
                var selectedItem by remember { mutableStateOf(0) }
                val items = listOf("Entrada", "Zona", "Total")

                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
            ) {
                Text(text = "Find App", style = MaterialTheme.typography.titleLarge)
            }
        }
    } else {
        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "Iniciando Sesión", style = MaterialTheme.typography.titleLarge)
                CircularProgressIndicator()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun MainScreenPreview() {
    FyndTheme {
        MainScreen(
            behavior = object : MainScreenViewModel.Behavior {
                override fun goToOnboardingScreen() {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}