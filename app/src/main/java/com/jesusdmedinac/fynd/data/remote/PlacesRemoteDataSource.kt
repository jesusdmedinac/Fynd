package com.jesusdmedinac.fynd.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.jesusdmedinac.fynd.data.remote.mapper.MapToPlaceMapper
import com.jesusdmedinac.fynd.data.remote.mapper.PlaceToMapMapper
import com.jesusdmedinac.fynd.data.remote.model.Place
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

interface PlacesRemoteDataSource {
    suspend fun setNumberOfPlacesFor(leaderEmail: String, number: Int)
    suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int>
    suspend fun getPlacesBy(leaderEmail: String): Flow<List<Place>>
    suspend fun updatePlacesBy(leaderEmail: String, places: List<Place>)
}

class PlacesRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mapToPlaceMapper: MapToPlaceMapper,
    private val placeToMapMapper: PlaceToMapMapper,
) : PlacesRemoteDataSource {
    override suspend fun setNumberOfPlacesFor(leaderEmail: String, number: Int) =
        suspendCancellableCoroutine { continuation ->
            firestore
                .collection("hosts")
                .document(leaderEmail)
                .update(
                    mapOf(
                        "numberOfPlaces" to number
                    )
                )
                .addOnCompleteListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.cancel(it)
                }
        }

    private var getNumberOfPlacesOfListenerRegistration: ListenerRegistration? = null

    override suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int> = callbackFlow {
        getNumberOfPlacesOfListenerRegistration?.run {
            remove()
            getNumberOfPlacesOfListenerRegistration = null
        }
        getNumberOfPlacesOfListenerRegistration = firestore
            .collection("hosts")
            .document(leaderEmail)
            .addSnapshotListener { nullableDocumentSnapshot, _ ->
                val data = nullableDocumentSnapshot.data()
                launch {
                    runCatching { "${data["numberOfPlaces"]}".toInt() }
                        .onSuccess { numberOfPlaces ->
                            send(numberOfPlaces)
                        }
                }
            }
        awaitClose()
    }
    private var getPlacesByListenerRegistration: ListenerRegistration? = null

    override suspend fun getPlacesBy(leaderEmail: String): Flow<List<Place>> = callbackFlow {
        getPlacesByListenerRegistration?.run {
            remove()
            getPlacesByListenerRegistration = null
        }
        getPlacesByListenerRegistration = firestore
            .collection("hosts")
            .document(leaderEmail)
            .collection("places")
            .addSnapshotListener { querySnapshot: QuerySnapshot?, _ ->
                runCatching { querySnapshot.data() }
                    .onSuccess { data ->
                        launch {
                            val places = data
                                .map { mapToPlaceMapper.map(it) }
                            send(places)
                        }
                    }
                    .onFailure { Log.e("dani", it.message.toString()) }
            }
        awaitClose()
    }

    override suspend fun updatePlacesBy(leaderEmail: String, places: List<Place>) =
        suspendCancellableCoroutine { continuation ->
            firestore
                .collection("hosts")
                .document(leaderEmail)
                .collection("places")
                .run {
                    places.forEach { place ->
                        document("${place.cell}")
                            .set(placeToMapMapper.map(place))
                            .addOnFailureListener {
                                continuation.cancel(it)
                            }
                    }
                    continuation.resume(Unit)
                }
        }
}