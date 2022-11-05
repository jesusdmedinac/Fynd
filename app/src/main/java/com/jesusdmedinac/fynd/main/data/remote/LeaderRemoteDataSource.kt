package com.jesusdmedinac.fynd.main.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

interface LeaderRemoteDataSource {
    fun joinBy(
        leaderCode: String,
        hostCode: String,
    )
}

class LeaderRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : LeaderRemoteDataSource {

    override fun joinBy(
        leaderCode: String,
        hostCode: String,
    ) {
        firestore
            .collection("host-teams")
            .document(leaderCode)
            .set(
                mapOf(
                    "host-code" to hostCode
                )
            )
    }
}