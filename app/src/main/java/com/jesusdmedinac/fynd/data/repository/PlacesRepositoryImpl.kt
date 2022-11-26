package com.jesusdmedinac.fynd.data.repository

import android.util.Log
import com.jesusdmedinac.fynd.data.local.PlaceDao
import com.jesusdmedinac.fynd.data.remote.PlacesRemoteDataSource
import com.jesusdmedinac.fynd.data.repository.mapper.DomainPlaceToRemotePlaceMapper
import com.jesusdmedinac.fynd.data.repository.mapper.LocalToDomainPlaceMapper
import com.jesusdmedinac.fynd.data.repository.mapper.RemotePlaceToLocalPlaceMapper
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
    private val placeDao: PlaceDao,
    private val remotePlaceToLocalPlaceMapper: RemotePlaceToLocalPlaceMapper,
    private val localToDomainPlaceMapper: LocalToDomainPlaceMapper,
    private val domainPlaceToRemotePlaceMapper: DomainPlaceToRemotePlaceMapper
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
            Log.d("dani leaderEmail", leaderEmail)
            placeDao.getPlacesBy(leaderEmail)

            placeDao.getPlacesFlowBy(leaderEmail)
                .map { places ->
                    Log.d("dani local places", "$places")
                    places.map { localToDomainPlaceMapper.map(it) }
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

    override suspend fun retrievePlacesBy(leaderEmail: String) {
        withContext(ioDispatcher) {
            placesRemoteDataSource.getPlacesBy(leaderEmail).map { places ->
                places.map { remotePlaceToLocalPlaceMapper.map(leaderEmail, it) }
            }
                .onEach { places ->
                    Log.d("dani remote places", "$places")
                    val missingLocalPlacesInRemote = placeDao.getPlacesBy(leaderEmail)
                        .filter { localPlace ->
                            places.none { remotePlace -> localPlace.cell == remotePlace.cell }
                        }
                    Log.d("dani mLIRemote", "$missingLocalPlacesInRemote")
                    missingLocalPlacesInRemote
                        .map { it.cell }
                        .forEach { cell ->
                            Log.d("dani cell", "$cell")
                            placeDao.deletePlace(leaderEmail, cell)
                        }
                }
                .collect { places ->
                    val localPlaces = placeDao.getPlacesBy(leaderEmail)
                    Log.d("dani localPlaces", "$localPlaces")
                    val missingRemotePlacesInLocal = places
                        .filter { remotePlace ->
                            localPlaces
                                .none { localPlace -> remotePlace.cell == localPlace.cell }
                        }
                    Log.d("dani mRILocal", "$missingRemotePlacesInLocal")
                    if (missingRemotePlacesInLocal.isNotEmpty()) {
                        Log.d("dani not empty", "$missingRemotePlacesInLocal")
                        placeDao.insertPlaces(missingRemotePlacesInLocal)
                    }
                }
        }
    }
}