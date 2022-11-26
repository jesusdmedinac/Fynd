package com.jesusdmedinac.fynd.data.remote.model

data class Place(
    val cell: String,
    val state: State,
) {
    enum class State {
        EMPTY, OCCUPIED, UNAVAILABLE
    }
}