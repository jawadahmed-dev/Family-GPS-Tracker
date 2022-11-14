package com.example.familygpstracker.apis

import com.example.familygpstracker.models.Notification
import com.example.familygpstracker.models.NotificationDto
import com.example.familygpstracker.models.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationService {

    @POST("notification/send")
    suspend fun sendNotification(
        @Body notificationDto: NotificationDto,
    ) : Response<NotificationResponse>

    @GET("notification")
    suspend fun getAllNotifications(
    ) : Response<List<Notification>>
}