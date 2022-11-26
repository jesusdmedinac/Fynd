package com.jesusdmedinac.fynd.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

fun DocumentSnapshot?.data(): Map<String, Any> {
    requireNotNull(this)
    require(exists())
    return data ?: throw IllegalArgumentException("Required data is null")
}

fun QuerySnapshot?.data(): List<Map<String, Any>> {
    requireNotNull(this)
    require(!isEmpty)
    return documents
        .map { it.data ?: throw IllegalArgumentException("Required data is null") }
}