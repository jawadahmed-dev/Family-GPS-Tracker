package com.example.familygpstracker.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familygpstracker.apis.NotificationService
import com.example.familygpstracker.models.Notification

class NotificationRepository (private val notificationService: NotificationService) {

    private var notificationListLiveData = MutableLiveData<List<Notification>>()

    val notifications : LiveData<List<Notification>>
    get() = notificationListLiveData

    suspend fun getAllNotifications(){
        var result = notificationService.getAllNotifications()
        if(result?.body() != null){
            notificationListLiveData.postValue(result.body())
        }
    }
}