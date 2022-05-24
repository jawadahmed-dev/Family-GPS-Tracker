package com.example.familygpstracker.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.familygpstracker.repositories.*

class MainViewModelFactory (private val userRepository: UserRepository,
                            private val parentRepository: ParentRepository,
                            private val notificationRepository: NotificationRepository,
                            private val locationRepository: LocationRepository,
                            private val geofenceRepository: GeofenceRepository,
                            private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(userRepository,parentRepository,notificationRepository,locationRepository,geofenceRepository,context) as T
    }
}