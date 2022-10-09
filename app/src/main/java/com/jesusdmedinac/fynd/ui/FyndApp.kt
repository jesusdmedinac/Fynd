package com.jesusdmedinac.fynd.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jesusdmedinac.fynd.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.viewmodel.FyndAppViewModel

@ExperimentalMaterial3Api
@Composable
fun FyndApp() {
    val fyndAppViewModel: FyndAppViewModel = hiltViewModel()

    val fyndAppState by fyndAppViewModel.container.stateFlow.collectAsState()
    val fyndAppSideEffect by fyndAppViewModel
        .container
        .sideEffectFlow
        .collectAsState(initial = FyndAppViewModel.SideEffect.Idle)

    val context = LocalContext.current

    LaunchedEffect(fyndAppSideEffect) {
        when (fyndAppSideEffect) {
            FyndAppViewModel.SideEffect.ColumnsLimitReached -> {
                Toast.makeText(context, "Intenta entre 0 y 10 columnas", Toast.LENGTH_SHORT).show()
            }
            FyndAppViewModel.SideEffect.RowsLimitReached -> {
                Toast.makeText(context, "Intenta entre 0 y 26 filas", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Scaffold(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            var columnsText = fyndAppState.columnsText
            var rowsText = fyndAppState.rowsText
            val invalidColumnsLimit = fyndAppState.invalidColumnsLimit
            val invalidRowsLimit = fyndAppState.invalidRowsLimit

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = columnsText,
                    onValueChange = { value ->
                        fyndAppViewModel.onColumnsValueChange(value)
                    },
                    placeholder = {
                        Text(text = "Columnas")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    isError = invalidColumnsLimit,
                    trailingIcon = {
                        if (invalidColumnsLimit) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    label = {
                        Text("Columnas")
                    },
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = rowsText,
                    onValueChange = { value ->
                        fyndAppViewModel.onRowsValueChange(value)
                    },
                    placeholder = {
                        Text(text = "Filas")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    isError = invalidRowsLimit,
                    trailingIcon = {
                        if (invalidRowsLimit) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    label = {
                        Text("Filas")
                    },
                )
            }

            val columns = if (fyndAppState.columns == 0) 1
            else fyndAppState.columns
            val total = fyndAppState.total

            when {
                invalidColumnsLimit || invalidRowsLimit -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        if (invalidColumnsLimit) {
                            Text(
                                text = "Intenta entre 0 y 10 columnas",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                        if (invalidRowsLimit) {
                            Text(
                                text = "Intenta entre 0 y 26 filas",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
                else -> {
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
                                    val cellText = fyndAppViewModel.cellText(cell, columns)
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
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun FyndAppPreview() {
    FyndTheme {
        FyndApp()
    }
}