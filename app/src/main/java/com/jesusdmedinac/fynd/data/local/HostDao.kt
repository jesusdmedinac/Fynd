package com.jesusdmedinac.fynd.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesusdmedinac.fynd.data.local.model.HostUser
import kotlinx.coroutines.flow.Flow

@Dao
interface HostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHostUser(hostUser: HostUser)

    @Query("SELECT * FROM hosts WHERE email = :email")
    fun getHostUser(email: String): HostUser

    @Query("SELECT count(*) FROM hosts WHERE email = :email AND isLoggedIn = 1")
    fun isLoggedIn(email: String): Boolean

    @Query("SELECT count(*) FROM hosts WHERE email = :email AND isLeader = 1")
    fun isLeader(email: String): Boolean

    @Query("SELECT * FROM hosts WHERE isLoggedIn = 1")
    fun loggedHostUserFlow(): Flow<HostUser?>

    @Query("SELECT * FROM hosts WHERE isLoggedIn = 1")
    fun loggedHostUser(): HostUser?

    @Query("SELECT * FROM hosts WHERE isLeader = 1")
    fun leaderHostUser(): HostUser?

    @Query("SELECT isOnboardingWelcomeScreenViewed FROM hosts WHERE isLoggedIn = 1")
    fun isOnboardingWelcomeScreenViewed(): Boolean

    @Query("UPDATE hosts SET isOnboardingWelcomeScreenViewed = :isOnboardingWelcomeScreenViewed WHERE isLoggedIn = 1")
    fun setIsOnboardingWelcomeScreenViewed(isOnboardingWelcomeScreenViewed: Boolean)
}