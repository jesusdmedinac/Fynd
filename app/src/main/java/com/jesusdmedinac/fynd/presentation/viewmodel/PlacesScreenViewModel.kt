package com.jesusdmedinac.fynd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.model.Host
import com.jesusdmedinac.fynd.domain.model.Place
import com.jesusdmedinac.fynd.domain.usecase.*
import com.jesusdmedinac.fynd.presentation.mapper.DomainPlaceToUiPlaceMapper
import com.jesusdmedinac.fynd.presentation.mapper.UiPlaceToDomainPlaceMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class PlacesScreenViewModel @Inject constructor(
    private val updatePlacesByLeaderEmailUseCase: UpdatePlacesByLeaderEmailUseCase,
    private val uiPlaceToDomainPlaceMapper: UiPlaceToDomainPlaceMapper,
    private val getPlacesByLeaderEmailUseCase: GetPlacesByLeaderEmailUseCase,
    private val domainPlaceToUiPlaceMapper: DomainPlaceToUiPlaceMapper,
    private val updateColumnsByLeaderEmailUseCase: UpdateColumnsByLeaderEmailUseCase,
    private val updateRowsByLeaderEmailUseCase: UpdateRowsByLeaderEmailUseCase,
    private val getUpdatedHostUseCase: GetUpdatedHostUseCase,
) : ViewModel(), PlacesScreenBehavior,
    ContainerHost<PlacesScreenViewModel.State, PlacesScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> = viewModelScope.container(State())

    data class State(
        val columnsText: String = "0",
        val rowsText: String = "0",
        val invalidColumnsLimit: Boolean = false,
        val invalidRowsLimit: Boolean = false,
        val places: List<Place> = emptyList(),
    ) {
        val total: Int
            get() = if (columnsText.isEmpty() || rowsText.isEmpty()) 0
            else {
                columns * rows
            }
        val columns: Int
            get() = if (columnsText.isEmpty()) 0
            else {
                val intValue = columnsText.toIntOrNull() ?: 0
                intValue + 1
            }

        val rows: Int
            get() = if (rowsText.isEmpty()) 0
            else {
                val intValue = rowsText.toIntOrNull() ?: 0
                intValue + 1
            }

        fun placeBy(cell: Int): Place? = places.firstOrNull { it.cell == cell }

        data class Place(
            val cell: Int,
            val state: State = State.OCCUPIED,
        ) {
            enum class State {
                EMPTY, OCCUPIED, UNAVAILABLE
            }

            fun isOccupied() = state == State.OCCUPIED
            fun isNotAvailable() = state == State.UNAVAILABLE
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

    lateinit var getPlacesByLeaderEmailUseCaseDeferred: Deferred<Result<Flow<List<Place>>>>
    lateinit var getCurrentHostUseCaseDeferred: Deferred<Unit>

    override fun onScreenLoad() {
        if (!::getPlacesByLeaderEmailUseCaseDeferred.isInitialized) {
            getPlacesByLeaderEmailUseCaseDeferred = viewModelScope.async {
                getPlacesByLeaderEmailUseCase()
                    .onFailure { Log.e("dani", it.message.toString()) }
                    .onSuccess { flowOfPlaces ->
                        flowOfPlaces.collect { places ->
                            intent {
                                reduce {
                                    val uiPlaces =
                                        places.map(domainPlaceToUiPlaceMapper::map)
                                    state.copy(places = uiPlaces)
                                }
                            }
                        }
                    }
            }
        }
        if (!::getCurrentHostUseCaseDeferred.isInitialized) {
            getCurrentHostUseCaseDeferred = viewModelScope.async {
                getUpdatedHostUseCase()
                    .collect { result ->
                        result
                            .onFailure { Log.d("dani", it.message.toString()) }
                            .onSuccess { host ->
                                onColumnsValueChange(host.columnsOfPlaces)
                                onRowsValueChange(host.rowsOfPlaces)
                            }
                    }
            }
        }
        intent {
            getPlacesByLeaderEmailUseCaseDeferred.await()
            getCurrentHostUseCaseDeferred.await()
        }
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
            updateColumnsByLeaderEmailUseCase(state.columnsText)
                .onFailure { Log.d("dani", it.message.toString()) }
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
            updateRowsByLeaderEmailUseCase(state.rowsText)
                .onFailure { Log.d("dani", it.message.toString()) }
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

    override fun onPlaceClick(cell: Int) {
        Log.w("dani", "onPlaceClick")
        intent {
            val places = state.places.toMutableList()
            val newPlaces = places.apply {
                val place = firstOrNull { it.cell == cell } ?: run {
                    add(State.Place(cell))
                    return@apply
                }

                val isOccupied = place.state == State.Place.State.OCCUPIED
                remove(place)
                if (isOccupied) {
                    add(place.copy(state = State.Place.State.EMPTY))
                } else {
                    add(place.copy(state = State.Place.State.OCCUPIED))
                }
            }
            reduce {
                state.copy(places = newPlaces)
            }
            updatePlacesByLeaderEmailUseCase(newPlaces.map(uiPlaceToDomainPlaceMapper::map))
        }
    }

    override fun onPlaceLongClick(cell: Int) {
        Log.w("dani", "onPlaceLongClick")
        intent {
            val places = state.places.toMutableList()
            val newPlaces = places.apply {
                val place = firstOrNull { it.cell == cell } ?: run {
                    add(State.Place(cell, State.Place.State.UNAVAILABLE))
                    return@apply
                }

                val isAvailable = place.state != State.Place.State.UNAVAILABLE
                remove(place)
                if (isAvailable) {
                    add(place.copy(state = State.Place.State.UNAVAILABLE))
                } else {
                    add(place.copy(state = State.Place.State.EMPTY))
                }
            }
            reduce {
                state.copy(places = newPlaces)
            }
            updatePlacesByLeaderEmailUseCase(newPlaces.map(uiPlaceToDomainPlaceMapper::map))
        }
    }

    companion object {
        private val COLUMNS_SIZE_RANGE = 0..10
        private val ROWS_SIZE_RANGE = 0..26
    }
}

interface PlacesScreenBehavior {
    fun onScreenLoad()
    fun onColumnsValueChange(columnsText: String)
    fun onRowsValueChange(rowsText: String)
    fun isCellRowHeader(cell: Int, columns: Int): Boolean
    fun isCellColumnHeader(cell: Int, columns: Int): Boolean
    fun cellText(cell: Int, columns: Int): String
    fun onPlaceClick(cell: Int)
    fun onPlaceLongClick(cell: Int)
}