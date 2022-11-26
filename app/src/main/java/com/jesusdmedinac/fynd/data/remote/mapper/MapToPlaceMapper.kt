package com.jesusdmedinac.fynd.data.remote.mapper

import com.jesusdmedinac.fynd.data.remote.model.Place
import javax.inject.Inject

class MapToPlaceMapper @Inject constructor() {
    fun map(input: Map<String, Any>): Place = with(input) {
        val cell: String = get("cell") as? String
            ?: throw IllegalArgumentException("Required cell is not an instance of Int")
        val stateAsString = get("state") as? String
            ?: throw IllegalArgumentException("Required state is not an instance of String")
        val state = Place.State.valueOf(stateAsString)
        Place(cell, state)
    }
}