package com.andrei.cerbulescu.placestracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlace(place: Place)

    @Query("SELECT * FROM place_table ORDER BY id Asc")
    fun readAllData(): LiveData<List<Place>>

    @Query("SELECT * FROM place_table ORDER BY ABS(latitude - :latitude) + ABS(longitude - :longitude) ASC")
    fun findByDistance(latitude:Double,longitude:Double): LiveData<List<Place>>
}