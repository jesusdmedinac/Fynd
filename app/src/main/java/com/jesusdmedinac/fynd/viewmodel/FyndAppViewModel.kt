package com.jesusdmedinac.fynd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class FyndAppViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<FyndAppViewModel.State, FyndAppViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    fun onColumnsValueChange(value: String) {
        intent {
            reduce { state.copy(columnsText = value) }
        }
    }

    fun onRowsValueChange(value: String) {
        intent {
            reduce { state.copy(rowsText = value) }
        }
    }

    fun onPlaceClicked() {
        intent {

        }
    }

    data class State(
        val columnsText: String = "1",
        val rowsText: String = "1",
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

    sealed class SideEffect
}