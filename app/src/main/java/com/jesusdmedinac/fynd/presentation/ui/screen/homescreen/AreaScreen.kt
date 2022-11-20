package com.jesusdmedinac.fynd.presentation.ui.screen.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun AreaScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { paddingValues ->
        Text("Area", modifier = Modifier.padding(paddingValues))
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun AreaScreenPreview() {
    FyndTheme {
        AreaScreen()
    }
}