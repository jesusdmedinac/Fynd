package com.jesusdmedinac.fynd.data.repository.mapper

import javax.inject.Inject
import com.jesusdmedinac.fynd.data.local.model.Place as LocalPlace
import com.jesusdmedinac.fynd.data.remote.model.Place as RemotePlace

class RemotePlaceToLocalPlaceMapper @Inject constructor() {
    fun map(leaderEmail: String, input: RemotePlace): LocalPlace = with(input) {
        val state = when (state) {
            RemotePlace.State.EMPTY -> LocalPlace.State.EMPTY
            RemotePlace.State.OCCUPIED -> LocalPlace.State.OCCUPIED
            RemotePlace.State.UNAVAILABLE -> LocalPlace.State.UNAVAILABLE
        }
        LocalPlace(
            leaderEmail,
            cell,
            state
        )
    }
}