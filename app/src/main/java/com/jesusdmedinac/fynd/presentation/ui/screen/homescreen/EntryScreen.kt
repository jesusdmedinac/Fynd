package com.jesusdmedinac.fynd.presentation.ui.screen.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun EntryScreen(
    onNumberClick: (Int) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        val screenHeight = LocalConfiguration.current.screenHeightDp

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            val buttonHeight = screenHeight / 3 - 32
            items(9) {
                val number = it + 1
                TextButton(
                    onClick = {
                        onNumberClick(number)
                    },
                    modifier = Modifier.height(buttonHeight.dp),
                ) {
                    Text(
                        "$number",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun EntryScreenPreview() {
    FyndTheme {
        EntryScreen()
    }
}