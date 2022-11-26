package com.jesusdmedinac.fynd.data.repository

import com.jesusdmedinac.fynd.data.remote.PlacesRemoteDataSource
import com.jesusdmedinac.fynd.data.repository.mapper.DomainPlaceToRemotePlaceMapper
import com.jesusdmedinac.fynd.data.repository.mapper.RemotePlaceToDomainPlaceMapper
import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named
import com.jesusdmedinac.fynd.data.remote.model.Place as RemotePlace
import com.jesusdmedinac.fynd.domain.model.Place as DomainPlace

class PlacesRepositoryImpl @Inject constructor(
    private val placesRemoteDataSource: PlacesRemoteDataSource,
    @Named("io-dispatcher")
    private val ioDispatcher: CoroutineDispatcher,
    private val domainPlaceToRemotePlaceMapper: DomainPlaceToRemotePlaceMapper,
    private val remotePlaceToDomainPlaceMapper: RemotePlaceToDomainPlaceMapper,
) : PlacesRepository {
    override suspend fun setCurrentNumberOfPlaces(leaderEmail: String, number: Int): Result<Unit> =
        try {
            placesRemoteDataSource.setNumberOfPlacesFor(leaderEmail, number)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }

    override suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int> =
        placesRemoteDataSource.getNumberOfPlacesOf(leaderEmail)

    override suspend fun getPlacesBy(leaderEmail: String): Flow<List<DomainPlace>> =
        withContext(ioDispatcher) {
            placesRemoteDataSource.getPlacesBy(leaderEmail).map { places ->
                places.map { remotePlaceToDomainPlaceMapper.map(it) }
            }
        }

    override suspend fun updatePlacesBy(
        leaderEmail: String,
        places: List<DomainPlace>,
    ): Result<Unit> =
        withContext(ioDispatcher) {
            val remotePlaces: List<RemotePlace> = places
                .map { domainPlaceToRemotePlaceMapper.map(it) }
            try {
                placesRemoteDataSource.updatePlacesBy(leaderEmail, remotePlaces)
                Result.success(Unit)
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
}