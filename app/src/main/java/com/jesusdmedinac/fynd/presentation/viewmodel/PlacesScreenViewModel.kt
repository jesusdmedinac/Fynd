package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class PlacesScreenViewModel @Inject constructor() :
    ViewModel(),
    PlacesScreenBehavior,
    ContainerHost<PlacesScreenViewModel.State, PlacesScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    data class State(
        val columnsText: String = "0",
        val rowsText: String = "0",
        val invalidColumnsLimit: Boolean = false,
        val invalidRowsLimit: Boolean = false,
        val places: Set<Place> = emptySet()
    ) {
        val total: Int
            get() = if (columnsText.isEmpty() || rowsText.isEmpty()) 0
            else {
                columns * rows
            }
        val columns: Int
            get() = if (columnsText.isEmpty()) 0
            else {
                columnsText.toInt() + 1
            }

        val rows: Int
            get() = if (rowsText.isEmpty()) 0
            else {
                rowsText.toInt() + 1
            }

        fun isPlaceOccupied(place: Place): Boolean = place in places

        data class Place(
            val cell: Int,
            val state: State
        ) {
            sealed class State {
                object Occupied : State()
                object Unavailable : State()
            }
        }
    }

    private suspend fun SimpleSyntax<State, SideEffect>.workAroundPostSideEffect(sideEffect: SideEffect) {
        postSideEffect(sideEffect)
        delay(100)
        postSideEffect(SideEffect.Idle)
    }

    sealed class SideEffect {
        object Idle : SideEffect()
        object ColumnsLimitReached : SideEffect()
        object RowsLimitReached : SideEffect()
    }

    override fun onColumnsValueChange(columnsText: String) {
        intent {
            val value = columnsText.toIntOrNull() ?: 0
            val invalidColumnsLimit = if (value !in COLUMNS_SIZE_RANGE) {
                workAroundPostSideEffect(SideEffect.ColumnsLimitReached)
                true
            } else {
                false
            }
            reduce {
                state.copy(
                    columnsText = columnsText,
                    invalidColumnsLimit = invalidColumnsLimit,
                )
            }
        }
    }

    override fun onRowsValueChange(rowsText: String) {
        intent {
            val value = rowsText.toIntOrNull() ?: 0
            val invalidRowsLimit = if (value !in ROWS_SIZE_RANGE) {
                workAroundPostSideEffect(SideEffect.RowsLimitReached)
                true
            } else {
                false
            }
            reduce {
                state.copy(
                    rowsText = rowsText,
                    invalidRowsLimit = invalidRowsLimit,
                )
            }
        }
    }

    override fun isCellRowHeader(cell: Int, columns: Int): Boolean {
        val cellBetweenColumns = cell.toDouble() / columns.toDouble()
        return cellBetweenColumns % 1 == 0.0
    }

    override fun isCellColumnHeader(cell: Int, columns: Int): Boolean = cell in 0 until columns

    override fun cellText(cell: Int, columns: Int): String {
        val cellBetweenColumns = cell.toDouble() / columns.toDouble()
        val isRowHeader = cellBetweenColumns % 1 == 0.0
        val alphabetLetters = ('a'..'z').toList()
        val amountOfAlphabetLetters = alphabetLetters.size
        val alphabetLetterCalculatedIndex = cellBetweenColumns.toInt() - 1
        val alphabetLetterIndex = alphabetLetterCalculatedIndex % amountOfAlphabetLetters

        return when {
            isRowHeader -> (alphabetLetterIndex + 1).toString()
            //alphabetLetters[alphabetLetterIndex]
            //.toString()
            //.uppercase()
            else -> cell.toString()
        }
    }

    override fun onPlaceClicked(place: State.Place) {
        intent {
            reduce {
                val places = state.places.toMutableSet()
                val newPlaces = places
                    .apply {
                        val isOccupied = (place in state.places
                                && place.state == State.Place.State.Occupied)
                        if (isOccupied) {
                            remove(place)
                        } else {
                            add(place)
                        }
                    }
                state.copy(places = newPlaces)
            }
        }
    }

    companion object {
        private val COLUMNS_SIZE_RANGE = 0..10
        private val ROWS_SIZE_RANGE = 0..26
    }
}

interface PlacesScreenBehavior {
    fun onColumnsValueChange(columnsText: String)
    fun onRowsValueChange(rowsText: String)
    fun isCellRowHeader(cell: Int, columns: Int): Boolean
    fun isCellColumnHeader(cell: Int, columns: Int): Boolean
    fun cellText(cell: Int, columns: Int): String
    fun onPlaceClicked(place: PlacesScreenViewModel.State.Place)
}