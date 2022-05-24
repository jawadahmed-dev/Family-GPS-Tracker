package com.example.familygpstracker.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familygpstracker.apis.GeofenceService
import com.example.familygpstracker.models.CreateGeofence
import com.example.familygpstracker.models.Geofence

class GeofenceRepository(private val geofenceService : GeofenceService) {

    private var geofenceListLiveData = MutableLiveData<List<Geofence>>()

    val geofenceList : LiveData<List<Geofence>>
        get() = geofenceListLiveData

    suspend fun createGeofence(childId:String, createGeofence: CreateGeofence) : Boolean{

        var result = geofenceService.createGeofence(childId, createGeofence)
        if(result?.body() != null && result?.code() == 200){
            return true
        }
        return false
    }

    suspend fun getGeofenceList(childId:String){

        var result = geofenceService.getGeofenceList(childId)

        if(result?.body() != null && result?.code() == 200){

            geofenceListLiveData.postValue(result.body())

        }
        else {
            Log.d("ExceptionMessage", ""+result?.message())
        }
    }

}