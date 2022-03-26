package com.example.familygpstracker.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familygpstracker.apis.LocationService
import com.example.familygpstracker.models.Location
import com.example.familygpstracker.models.Notification

class LocationRepository (private val locationService: LocationService) {
    private var locationLiveData = MutableLiveData<Location>()

    val location : LiveData<Location>
        get() = locationLiveData

    suspend fun getLastLocation(childId:String){

        var result = locationService.getLastLocation(childId)
        if(result?.body() != null && result?.code() == 200){
            locationLiveData.postValue(result.body())
        }
    }
}