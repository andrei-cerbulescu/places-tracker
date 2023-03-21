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
}