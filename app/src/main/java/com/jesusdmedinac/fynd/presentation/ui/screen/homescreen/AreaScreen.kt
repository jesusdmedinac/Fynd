package com.jesusdmedinac.fynd.presentation.ui.screen.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun AreaScreen(
    numberOfPlaces: Int,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "$numberOfPlaces",
                modifier = Modifier.padding(paddingValues),
                fontSize = 312.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun AreaScreenPreview() {
    FyndTheme {
        AreaScreen(9)
    }
}