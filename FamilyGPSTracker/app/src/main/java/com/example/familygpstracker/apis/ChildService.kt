package com.example.familygpstracker.apis

import com.example.familygpstracker.models.ChildDetail
import com.example.familygpstracker.models.PairingCode
import com.example.familygpstracker.models.Parent
import com.example.familygpstracker.models.RegisterParent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChildService {

    @GET("child/code/{id}")
    suspend fun getPairingCode(
        @Path("id") childId : String
    ) : Response<PairingCode>

    @POST("child/LinkParent/{parentId}")
    suspend fun linkParent(
        @Path("parentId") parentId : String,
        @Body pairingCode: PairingCode,
    ) : Response<ChildDetail>

    @POST("child/{id}}")
    suspend fun getChildDetail(
        @Path("id") childId: String,
    ) : Response<ChildDetail>

}