package com.andrei.cerbulescu.placestracker.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlace(place: Place)

    @Delete
    fun deletePlace(place: Place)

    @Query("SELECT * FROM place_table ORDER BY id Asc")
    fun readAllData(): LiveData<List<Place>>

    @Query("SELECT * FROM place_table ORDER BY ABS(latitude - :latitude) + ABS(longitude - :longitude) ASC LIMIT 1")
    fun findFirstByDistance(latitude:Double,longitude:Double): LiveData<Place>

    @Query("SELECT * FROM place_table WHERE ((latitude - :latitude) + ABS(longitude - :longitude)) < :distance")
    fun findByDistance(latitude:Double, longitude:Double, distance:Double): LiveData<List<Place>>

    @Query("SELECT * FROM place_table WHERE id = :id LIMIT 1")
    fun findById(id: Int): LiveData<Place>
}