package com.jesusdmedinac.fynd.viewmodel

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
class FyndAppViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<FyndAppViewModel.State, FyndAppViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    fun onColumnsValueChange(textValue: String) {
        intent {
            val value = textValue.toIntOrNull() ?: 0
            val invalidColumnsLimit = if (value !in COLUMNS_SIZE_RANGE) {
                workAroundPostSideEffect(SideEffect.ColumnsLimitReached)
                true
            } else {
                false
            }
            reduce {
                state.copy(
                    columnsText = textValue,
                    invalidColumnsLimit = invalidColumnsLimit,
                )
            }
        }
    }

    fun onRowsValueChange(textValue: String) {
        intent {
            val value = textValue.toIntOrNull() ?: 0
            val invalidRowsLimit = if (value !in ROWS_SIZE_RANGE) {
                workAroundPostSideEffect(SideEffect.RowsLimitReached)
                true
            } else {
                false
            }
            reduce {
                state.copy(
                    rowsText = textValue,
                    invalidRowsLimit = invalidRowsLimit,
                )
            }
        }
    }

    fun onPlaceClicked() {
        intent {

        }
    }

    fun cellText(cell: Int, columns: Int): String {
        val cellBetweenColumns = cell.toDouble() / columns.toDouble()
        val isRowHeader = cellBetweenColumns % 1 == 0.0
        val alphabetLetters = ('a'..'z').toList()
        val amountOfAlphabetLetters = alphabetLetters.size
        val alphabetLetterCalculatedIndex = cellBetweenColumns.toInt() - 1
        val alphabetLetterIndex = alphabetLetterCalculatedIndex % amountOfAlphabetLetters

        return when {
            isRowHeader -> alphabetLetters[alphabetLetterIndex]
                .toString()
                .uppercase()
            else -> cell.toString()
        }
    }

    data class State(
        val columnsText: String = "1",
        val rowsText: String = "1",
        val invalidColumnsLimit: Boolean = false,
        val invalidRowsLimit: Boolean = false,
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

    companion object {
        private val COLUMNS_SIZE_RANGE = 0..10
        private val ROWS_SIZE_RANGE = 0..26
    }
}
