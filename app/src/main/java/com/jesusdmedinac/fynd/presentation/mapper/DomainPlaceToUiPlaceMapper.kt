package com.jesusdmedinac.fynd.presentation.mapper

import javax.inject.Inject
import com.jesusdmedinac.fynd.domain.model.Place as DomainPlace
import com.jesusdmedinac.fynd.presentation.viewmodel.PlacesScreenViewModel.State.Place as UiPlace

class DomainPlaceToUiPlaceMapper @Inject constructor() {
    fun map(input: DomainPlace): UiPlace = with(input) {
        val state = when (state) {
            DomainPlace.State.EMPTY -> UiPlace.State.EMPTY
            DomainPlace.State.OCCUPIED -> UiPlace.State.OCCUPIED
            DomainPlace.State.UNAVAILABLE -> UiPlace.State.UNAVAILABLE
        }
        val cell = cell.toInt()
        UiPlace(
            cell,
            state
        )
    }
}