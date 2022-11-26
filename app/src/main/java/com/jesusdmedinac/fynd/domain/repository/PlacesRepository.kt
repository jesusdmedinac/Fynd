package com.jesusdmedinac.fynd.domain.repository

import com.jesusdmedinac.fynd.domain.model.Place
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    suspend fun setCurrentNumberOfPlaces(leaderEmail: String, number: Int): Result<Unit>
    suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int>
    suspend fun getPlacesBy(leaderEmail: String): Flow<List<Place>>
    suspend fun updatePlacesBy(leaderEmail: String, places: List<Place>): Result<Unit>
}