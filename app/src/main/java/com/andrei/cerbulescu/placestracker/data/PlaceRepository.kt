package com.andrei.cerbulescu.placestracker.data

import androidx.lifecycle.LiveData

class PlaceRepository(private val placeDao: PlaceDao) {
    val readAllData: LiveData<List<Place>> = placeDao.readAllData()

    suspend fun addPlace(place: Place){
        placeDao.addPlace(place)
    }

    fun findByDistance(latitude: Double, longitude: Double): LiveData<List<Place>> {
        return placeDao.findByDistance(latitude,longitude)
    }
}