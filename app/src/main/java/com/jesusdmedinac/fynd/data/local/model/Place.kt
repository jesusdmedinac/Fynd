package com.jesusdmedinac.fynd.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "places",
    primaryKeys = ["email", "cell"],
    foreignKeys = [
        ForeignKey(
            entity = HostUser::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class Place(
    val email: String,
    val cell: String,
    val state: State,
) {
    enum class State {
        EMPTY, OCCUPIED, UNAVAILABLE
    }
}