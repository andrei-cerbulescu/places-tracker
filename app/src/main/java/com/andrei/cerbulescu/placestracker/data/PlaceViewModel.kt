package com.andrei.cerbulescu.placestracker.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Place>>
    private val repository: PlaceRepository

    init{
        val placeDao = AppDatabase.getDatabase(application).placeDao()
        repository = PlaceRepository(placeDao)
        readAllData = repository.readAllData
    }

    fun addPlace(place: Place){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPlace(place)
        }
    }

    fun findFirstByDistance(lat: Double, long: Double): LiveData<Place>{
        return repository.findFirstByDistance(lat, long)
    }

    fun findFirstById(id: Int): LiveData<Place>{
        return repository.findById(id)
    }

    fun findByDistance(latitude:Double,longitude:Double, distance:Double): LiveData<List<Place>>{
        return repository.findByDistance(latitude, longitude, distance)
    }

    fun deletePlace(place: Place){
        viewModelScope.launch(Dispatchers.IO){
            repository.deletePlace(place)
        }
    }
}