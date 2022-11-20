package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.remote.PlacesRemoteDataSource
import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesRemoteDataSource: PlacesRemoteDataSource,
) : PlacesRepository {
    override suspend fun setCurrentNumberOfPlacesUseCase(leaderEmail: String, number: Int) {
        placesRemoteDataSource.setNumberOfPlacesFor(leaderEmail, number)
    }
}