package com.example.familygpstracker.apis

import com.example.familygpstracker.models.CreateGeofence
import com.example.familygpstracker.models.Geofence
import com.example.familygpstracker.models.Location
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GeofenceService {

    @GET("/geofences/list/{childId}")
    suspend fun getGeofenceList(
        @Path("childId") childId : String
    ): Response<List<Geofence>>

    @POST("/geofences/{childId}")
    suspend fun createGeofence(
        @Path("childId") childId : String,
        @Body createGeofence : CreateGeofence
    ): Response<Geofence>

}