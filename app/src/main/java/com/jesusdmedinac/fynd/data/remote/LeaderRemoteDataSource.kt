package com.jesusdmedinac.fynd.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.jesusdmedinac.fynd.data.remote.mapper.MapToHostUserMapper
import com.jesusdmedinac.fynd.data.remote.model.HostUser
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

interface LeaderRemoteDataSource {
    suspend fun joinBy(
        leaderCode: String,
        hostCode: String,
    ): HostUser
}

class LeaderRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mapToHostUserMapper: MapToHostUserMapper,
) : LeaderRemoteDataSource {
    override suspend fun joinBy(
        leaderCode: String,
        hostCode: String,
    ): HostUser {
        val hostDocument: DocumentSnapshot = hostDocumentBy(hostCode)
        val leaderDocumentSnapshot: DocumentSnapshot = leaderDocumentSnapshotBy(leaderCode)
        return hostUserBy(hostDocument, leaderDocumentSnapshot)
    }

    private suspend fun hostUserBy(
        hostDocument: DocumentSnapshot,
        leaderDocumentSnapshot: DocumentSnapshot
    ) = suspendCancellableCoroutine { continuation ->
        hostDocument
            .reference
            .update("leaderCode", leaderDocumentSnapshot.reference)
            .addOnSuccessListener {
                leaderDocumentSnapshot.data?.let { data ->
                    continuation.resume(mapToHostUserMapper.map(data))
                }
            }
            .addOnFailureListener { continuation.cancel(it) }
    }

    private suspend fun leaderDocumentSnapshotBy(leaderCode: String): DocumentSnapshot =
        suspendCancellableCoroutine { continuation ->
            firestore
                .collection("hosts")
                .whereEqualTo("qrCode", leaderCode)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    runCatching { querySnapshot.firstDocumentBy(leaderCode) }
                        .onSuccess(continuation::resume)
                        .onFailure(continuation::cancel)
                }
                .addOnFailureListener { continuation.cancel(it) }
        }

    private suspend fun hostDocumentBy(hostCode: String) =
        suspendCancellableCoroutine { continuation ->
            firestore
                .collection("hosts")
                .whereEqualTo("qrCode", hostCode)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    runCatching { querySnapshot.firstDocumentBy(hostCode) }
                        .onSuccess(continuation::resume)
                        .onFailure(continuation::cancel)
                }
                .addOnFailureListener { continuation.cancel(it) }
        }

    private fun QuerySnapshot.firstDocumentBy(
        hostCode: String,
    ): DocumentSnapshot = when {
        documents.size == 1 -> {
            documents.first()
        }
        documents.size > 1 -> {
            throw Throwable("More than one host found for code $hostCode")
        }
        documents.size < 1 -> {
            throw Throwable("No hosts found for code $hostCode")
        }
        else -> {
            throw Throwable("Unknown error for code $hostCode")
        }
    }
}