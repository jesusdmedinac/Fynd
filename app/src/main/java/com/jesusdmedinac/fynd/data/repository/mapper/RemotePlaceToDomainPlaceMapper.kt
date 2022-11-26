package com.jesusdmedinac.fynd.data.repository.mapper

import javax.inject.Inject
import com.jesusdmedinac.fynd.data.remote.model.Place as RemotePlace
import com.jesusdmedinac.fynd.domain.model.Place as DomainPlace

class RemotePlaceToDomainPlaceMapper @Inject constructor() {
    fun map(input: RemotePlace): DomainPlace = with(input) {
        DomainPlace(
            cell, when (state) {
                RemotePlace.State.EMPTY -> DomainPlace.State.EMPTY
                RemotePlace.State.OCCUPIED -> DomainPlace.State.OCCUPIED
                RemotePlace.State.UNAVAILABLE -> DomainPlace.State.UNAVAILABLE
            }
        )
    }
}