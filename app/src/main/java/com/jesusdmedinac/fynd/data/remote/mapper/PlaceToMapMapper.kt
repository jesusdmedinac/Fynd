package com.jesusdmedinac.fynd.data.remote.mapper

import com.jesusdmedinac.fynd.data.remote.model.Place
import javax.inject.Inject

class PlaceToMapMapper @Inject constructor() {
    fun map(input: Place): Map<String, Any> = with(input) {
        mapOf(
            "cell" to cell,
            "state" to state.toString()
        )
    }
}