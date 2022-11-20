package com.jesusdmedinac.fynd.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

interface PlacesRemoteDataSource {
    suspend fun setNumberOfPlacesFor(leaderEmail: String, number: Int)
    suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int>
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

    override suspend fun getNumberOfPlacesOf(leaderEmail: String): Flow<Int> = callbackFlow {
        firestore
            .collection("hosts")
            .document(leaderEmail)
            .addSnapshotListener { nullableDocumentSnapshot, firebaseFirestoreException ->
                val documentSnapshot = nullableDocumentSnapshot ?: return@addSnapshotListener
                if (!documentSnapshot.exists()) return@addSnapshotListener
                val data = documentSnapshot.data ?: return@addSnapshotListener
                launch {
                    runCatching { "${data["numberOfPlaces"]}".toInt() }
                        .onSuccess { numberOfPlaces ->
                            send(numberOfPlaces)
                        }
                }
            }
        awaitClose()
    }
}