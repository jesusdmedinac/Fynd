package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.remote.PlacesRemoteDataSource
import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesRemoteDataSource: PlacesRemoteDataSource,
) : PlacesRepository {
    override suspend fun setCurrentNumberOfPlaces(leaderEmail: String, number: Int) {
        placesRemoteDataSource.setNumberOfPlacesFor(leaderEmail, number)
    }

    override suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int> =
        placesRemoteDataSource.getNumberOfPlacesOf(leaderEmail)
}