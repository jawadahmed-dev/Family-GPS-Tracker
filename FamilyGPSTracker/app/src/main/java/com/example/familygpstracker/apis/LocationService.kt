package com.example.familygpstracker.apis

import com.example.familygpstracker.models.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationService {

    @GET("/location/lastLocation/{childId}")
    suspend fun getLastLocation(
        @Path("childId") childId : String
    ): Response<Location>
}