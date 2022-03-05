package com.example.familygpstracker.apis

import com.example.familygpstracker.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("User/{id}/userDetails")
    suspend fun getUserDetails(
        @Path("id") userId : String
    ) : Response<User>

    @GET("User/{email}")
    suspend fun getUser(
        @Path("email") email : String
    ) : Response<User>

}