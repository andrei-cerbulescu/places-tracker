package com.andrei.cerbulescu.placestracker.data

import androidx.lifecycle.LiveData

class PlaceRepository(private val placeDao: PlaceDao) {
    val readAllData: LiveData<List<Place>> = placeDao.readAllData()

    suspend fun addPlace(place: Place){
        placeDao.addPlace(place)
    }

    fun findFirstByDistance(latitude: Double, longitude: Double): LiveData<Place> {
        return placeDao.findFirstByDistance(latitude,longitude)
    }

    fun findById(id: Int): LiveData<Place>{
        return placeDao.findById(id)
    }

    suspend fun deletePlace(place: Place){
        placeDao.deletePlace(place)
    }
}