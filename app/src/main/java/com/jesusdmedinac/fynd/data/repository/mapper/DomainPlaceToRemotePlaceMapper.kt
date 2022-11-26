package com.jesusdmedinac.fynd.data.repository.mapper

import javax.inject.Inject
import com.jesusdmedinac.fynd.data.remote.model.Place as RemotePlace
import com.jesusdmedinac.fynd.domain.model.Place as DomainPlace

class DomainPlaceToRemotePlaceMapper @Inject constructor() {
    fun map(input: DomainPlace): RemotePlace = with(input) {
        RemotePlace(cell, when (state) {
            DomainPlace.State.EMPTY -> RemotePlace.State.EMPTY
            DomainPlace.State.OCCUPIED -> RemotePlace.State.OCCUPIED
            DomainPlace.State.UNAVAILABLE -> RemotePlace.State.UNAVAILABLE
        })
    }
}