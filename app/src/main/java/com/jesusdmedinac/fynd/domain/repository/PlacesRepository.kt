package com.jesusdmedinac.fynd.domain.repository

interface PlacesRepository {
    suspend fun setCurrentNumberOfPlacesUseCase(leaderEmail: String, number: Int)
}