package com.example.familygpstracker.apis

import com.example.familygpstracker.models.Parent
import com.example.familygpstracker.models.ParentDetail
import com.example.familygpstracker.models.RegisterParent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ParentService {

    @POST("parent")
    suspend fun registerParent(
        @Body parent : RegisterParent
    ) : Response<Parent>

    @GET("parent/details/{id}")
    suspend fun getParentDetails(
        @Path("id") id : String
    ) : Response<ParentDetail>

}