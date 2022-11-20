package com.jesusdmedinac.fynd.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

interface PlacesRemoteDataSource {
    suspend fun setNumberOfPlacesFor(leaderEmail: String, number: Int)
}

class PlacesRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : PlacesRemoteDataSource {
    override suspend fun setNumberOfPlacesFor(leaderEmail: String, number: Int) {
        firestore
            .collection("hosts")
            .document(leaderEmail)
            .update(
                mapOf(
                    "numberOfPlaces" to number
                )
            )
    }
}