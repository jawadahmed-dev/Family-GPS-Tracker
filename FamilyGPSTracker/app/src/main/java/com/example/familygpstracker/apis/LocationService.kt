package com.example.familygpstracker.apis

import com.example.familygpstracker.models.Location
import com.example.familygpstracker.models.LocationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LocationService {

    @GET("/location/lastLocation/{childId}")
    suspend fun getLastLocation(
        @Path("childId") childId : String
    ): Response<Location>

    @POST("/location/{childId}")
    suspend fun postLocation(
        @Path("childId") childId : String,
        @Body locationDto : LocationDto
    ): Response<Location>

    @GET("/location/lastTenLocations/{childId}")
    suspend fun getLastTenLocations(
        @Path("childId") childId : String
    ): Response<List<Location>>
}