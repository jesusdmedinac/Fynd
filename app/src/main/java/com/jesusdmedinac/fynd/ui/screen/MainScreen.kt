package com.jesusdmedinac.fynd.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jesusdmedinac.fynd.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun MainScreen() {
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
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun MainScreenPreview() {
    FyndTheme {
        MainScreen()
    }
}