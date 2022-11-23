package com.jesusdmedinac.fynd.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "hosts"
)
data class HostUser(
    @PrimaryKey
    val email: String,
    val photoUrl: String,
    val displayName: String,
    val qrCode: String,
    val isLoggedIn: Boolean,
    val isLeader: Boolean,
    val isOnboardingWelcomeScreenViewed: Boolean,
)