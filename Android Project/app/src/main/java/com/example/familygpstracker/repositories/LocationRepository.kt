package com.example.familygpstracker.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familygpstracker.apis.LocationService
import com.example.familygpstracker.models.Location

class LocationRepository (private val locationService: LocationService) {
    private var locationLiveData = MutableLiveData<Location>()
    private var lastTenlocationsLiveData = MutableLiveData<List<Location>>()

    val location : LiveData<Location>
        get() = locationLiveData

    val lastTenLocations : LiveData<List<Location>>
        get() = lastTenlocationsLiveData

    suspend fun getLastLocation(childId:String){

        var result = locationService.getLastLocation(childId)
        if(result?.body() != null && result?.code() == 200){
            locationLiveData.postValue(result.body())
        }
    }

    suspend fun getLastTenLocations(childId:String){

        var result = locationService.getLastTenLocations(childId)
        if(result?.body() != null && result?.code() == 200){
            lastTenlocationsLiveData.postValue(result.body())
        }
    }
}