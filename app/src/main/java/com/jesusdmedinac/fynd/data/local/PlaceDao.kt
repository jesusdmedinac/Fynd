package com.jesusdmedinac.fynd.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesusdmedinac.fynd.data.local.model.Place
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaces(places: List<Place>)

    @Query("SELECT * FROM places WHERE email = :email")
    fun getPlacesFlowBy(email: String): Flow<List<Place>>

    @Query("SELECT * FROM places WHERE email = :email")
    fun getPlacesBy(email: String): List<Place>

    @Query("DELETE FROM places WHERE cell = :cell AND email = :email")
    fun deletePlace(cell: String, email: String)
}