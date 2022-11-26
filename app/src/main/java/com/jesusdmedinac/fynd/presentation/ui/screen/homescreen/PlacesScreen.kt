package com.jesusdmedinac.fynd.presentation.ui.screen.homescreen

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.PlacesScreenBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.PlacesScreenViewModel

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun PlacesScreen(
    placesScreenState: PlacesScreenViewModel.State = PlacesScreenViewModel.State(),
    placesScreenSideEffect: PlacesScreenViewModel.SideEffect = PlacesScreenViewModel.SideEffect.Idle,
    placesScreenBehavior: PlacesScreenBehavior,
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

    LaunchedEffect(Unit) {
        placesScreenBehavior.onScreenLoad()
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
                        placesScreenBehavior.onColumnsValueChange(value)
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
                        placesScreenBehavior.onRowsValueChange(value)
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
                            val isColumnHeader = placesScreenBehavior.isCellRowHeader(cell, columns)
                            val isRowHeader = placesScreenBehavior.isCellColumnHeader(cell, columns)
                            if (cell == 0) return@items
                            if (isColumnHeader || isRowHeader) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val cellText = placesScreenBehavior.cellText(cell, columns)
                                    Text(
                                        text = cellText,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            } else {
                                PlaceButton(
                                    modifier = Modifier.height(64.dp),
                                    placesScreenState,
                                    cell,
                                    onPlaceClick = placesScreenBehavior::onPlaceClick,
                                    onPlaceLongClick = placesScreenBehavior::onPlaceLongClick,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun PlaceButton(
    modifier: Modifier = Modifier,
    placesScreenState: PlacesScreenViewModel.State,
    cell: Int,
    onPlaceLongClick: (Int) -> Unit,
    onPlaceClick: (Int) -> Unit,
) {
    val buttonBackground = MaterialTheme.colorScheme.run {
        val place =
            placesScreenState.placeBy(cell) ?: return@run onPrimary
        when {
            place.isOccupied() -> primary
            place.isNotAvailable() -> error
            else -> onPrimary
        }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                BorderStroke(
                    4.dp,
                    MaterialTheme.colorScheme.primary
                )
            )
            .background(buttonBackground)
            .combinedClickable(
                onLongClick = {
                    onPlaceLongClick(cell)
                },
                onClick = {
                    onPlaceClick(cell)
                },
            ),
    )
}

@ExperimentalFoundationApi
@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
fun PlaceButtonPreview() {
    FyndTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(4) {
                PlaceButton(
                    modifier = Modifier.height(64.dp),
                    placesScreenState = PlacesScreenViewModel.State(),
                    cell = 1,
                    onPlaceLongClick = {},
                    onPlaceClick = {},
                )
            }
        }
    }
}