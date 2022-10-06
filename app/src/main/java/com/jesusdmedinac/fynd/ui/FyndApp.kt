package com.jesusdmedinac.fynd.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.ui.theme.FyndTheme

@ExperimentalMaterial3Api
@Composable
fun FyndApp() {
    Scaffold(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            var columnsText by remember { mutableStateOf("8") }
            var rowsText by remember { mutableStateOf("10") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = columnsText,
                    onValueChange = { value ->
                        when {
                            value.isEmpty() -> {}
                            else -> {}
                        }
                        columnsText = value
                    },
                    placeholder = {
                        Text(text = "Columns")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = rowsText,
                    onValueChange = { value ->
                        when {
                            value.isEmpty() -> {}
                            else -> {}
                        }
                        rowsText = value
                    },
                    placeholder = {
                        Text(text = "Rows")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            if (columnsText.isNotEmpty() && rowsText.isNotEmpty()) {
                val columns = columnsText.toInt()
                val rows = rowsText.toInt()
                val total = columns * rows
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(total) { cell ->
                        val isColumnHeader = cell in 0 until columns
                        val cellBetweenColumns = cell.toDouble() / columns.toDouble()
                        val isRowHeader = cellBetweenColumns % 1 == 0.0
                        if (cell == 0) return@items
                        if (isColumnHeader || isRowHeader) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                val alphabetLetters = ('a'..'z').toList()
                                val cellText =
                                    if (isRowHeader) alphabetLetters[cellBetweenColumns.toInt() - 1]
                                        .toString()
                                        .uppercase()
                                    else cell.toString()
                                Text(
                                    text = cellText,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        } else {
                            Button(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .size(42.dp)
                                    .background(MaterialTheme.colorScheme.primary),
                                onClick = {}
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun FyndAppPreview() {
    FyndTheme {
        FyndApp()
    }
}