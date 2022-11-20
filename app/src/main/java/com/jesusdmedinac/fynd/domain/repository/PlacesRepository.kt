package com.jesusdmedinac.fynd.domain.repository

import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    suspend fun setCurrentNumberOfPlaces(leaderEmail: String, number: Int)
    suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int>
}