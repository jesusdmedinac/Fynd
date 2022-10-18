package com.jesusdmedinac.fynd.main.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.main.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.places.presentation.viewmodel.PlacesScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun PlacesScreen(
    placesScreenState: PlacesScreenViewModel.State = PlacesScreenViewModel.State(),
    placesScreenSideEffect: PlacesScreenViewModel.SideEffect = PlacesScreenViewModel.SideEffect.Idle,
    behavior: PlacesScreenViewModel.Behavior,
) {
    val context = LocalContext.current

    LaunchedEffect(placesScreenSideEffect) {
        when (placesScreenSideEffect) {
            PlacesScreenViewModel.SideEffect.ColumnsLimitReached -> {
                Toast.makeText(context, "Intenta entre 0 y 10 columnas", Toast.LENGTH_SHORT).show()
            }
            PlacesScreenViewModel.SideEffect.RowsLimitReached -> {
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
            var columnsText = placesScreenState.columnsText
            var rowsText = placesScreenState.rowsText
            val invalidColumnsLimit = placesScreenState.invalidColumnsLimit
            val invalidRowsLimit = placesScreenState.invalidRowsLimit

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = columnsText,
                    onValueChange = { value ->
                        behavior.onColumnsValueChange(value)
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
                        behavior.onRowsValueChange(value)
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

            val columns = if (placesScreenState.columns == 0) 1
            else placesScreenState.columns
            val total = placesScreenState.total

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
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(total) { cell ->
                            val isColumnHeader = behavior.isCellRowHeader(cell, columns)
                            val isRowHeader = behavior.isCellColumnHeader(cell, columns)
                            if (cell == 0) return@items
                            if (isColumnHeader || isRowHeader) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val cellText = behavior.cellText(cell, columns)
                                    Text(
                                        text = cellText,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            } else {
                                val occupiedPlace = PlacesScreenViewModel.State.Place(
                                    cell,
                                    PlacesScreenViewModel.State.Place.State.Occupied
                                )
                                val buttonBackground = MaterialTheme.colorScheme.run {
                                    if (placesScreenState.isPlaceOccupied(occupiedPlace))
                                        primary
                                    else onPrimary
                                }
                                    Button(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        onClick = {
                                            behavior.onPlaceClicked(occupiedPlace)
                                        },
                                        border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = buttonBackground,
                                        ),
                                        content = {},
                                        shape = RoundedCornerShape(8.dp)
                                    )
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
        PlacesScreen(
            behavior = object : PlacesScreenViewModel.Behavior{
                override fun onColumnsValueChange(columnsText: String) {
                    TODO("Not yet implemented")
                }

                override fun onRowsValueChange(rowsText: String) {
                    TODO("Not yet implemented")
                }

                override fun isCellRowHeader(cell: Int, columns: Int): Boolean {
                    TODO("Not yet implemented")
                }

                override fun isCellColumnHeader(cell: Int, columns: Int): Boolean {
                    TODO("Not yet implemented")
                }

                override fun cellText(cell: Int, columns: Int): String {
                    TODO("Not yet implemented")
                }

                override fun onPlaceClicked(place: PlacesScreenViewModel.State.Place) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}