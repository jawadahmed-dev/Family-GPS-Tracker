package com.example.familygpstracker.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familygpstracker.models.Notification
import com.example.familygpstracker.repositories.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel (
    private var repository : NotificationRepository
        ) : ViewModel() {

    init {
        viewModelScope.launch (Dispatchers.IO){
            repository.getAllNotifications()
        }
    }

    val notificationList: LiveData<List<Notification>>
        get() = repository.notifications

}