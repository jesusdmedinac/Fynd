package com.jesusdmedinac.fynd.presentation.mapper

import com.jesusdmedinac.fynd.domain.model.Place
import com.jesusdmedinac.fynd.presentation.viewmodel.PlacesScreenViewModel
import javax.inject.Inject

class UiPlaceToDomainPlaceMapper @Inject constructor() {
    fun map(input: PlacesScreenViewModel.State.Place): Place = with(input) {
        val state = when (state) {
            PlacesScreenViewModel.State.Place.State.EMPTY -> Place.State.EMPTY
            PlacesScreenViewModel.State.Place.State.OCCUPIED -> Place.State.OCCUPIED
            PlacesScreenViewModel.State.Place.State.UNAVAILABLE -> Place.State.UNAVAILABLE
        }
        Place("$cell", state)
    }
}