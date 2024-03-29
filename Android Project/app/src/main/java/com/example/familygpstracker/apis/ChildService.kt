package com.example.familygpstracker.apis

import com.example.familygpstracker.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChildService {

    @POST("child/register")
    suspend fun registerChild(
        @Body parent : RegisterChild
    ) : Response<Child>

    @GET("child/code/{id}")
    suspend fun getPairingCode(
        @Path("id") childId : String
    ) : Response<PairingCode>

    @POST("child/LinkParent/{parentId}")
    suspend fun linkParent(
        @Path("parentId") parentId : String,
        @Body pairingCode: PairingCode,
    ) : Response<ChildDetail>

    @GET("child/details/{id}")
    suspend fun getChildDetail(
        @Path("id") childId: String,
    ) : Response<ChildDetail>

}