package com.jesusdmedinac.fynd.data.repository.mapper

import javax.inject.Inject
import com.jesusdmedinac.fynd.data.local.model.Place as LocalPlace
import com.jesusdmedinac.fynd.domain.model.Place as DomainPlace

class LocalToDomainPlaceMapper @Inject constructor() {
    fun map(input: LocalPlace): DomainPlace = with(input) {
        DomainPlace(cell, when (state) {
            LocalPlace.State.EMPTY -> DomainPlace.State.EMPTY
            LocalPlace.State.OCCUPIED -> DomainPlace.State.OCCUPIED
            LocalPlace.State.UNAVAILABLE -> DomainPlace.State.UNAVAILABLE
        })
    }
}