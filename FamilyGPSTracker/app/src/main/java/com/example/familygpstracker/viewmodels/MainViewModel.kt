package com.example.familygpstracker.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familygpstracker.models.Location
import com.example.familygpstracker.models.Notification
import com.example.familygpstracker.models.ParentDetail
import com.example.familygpstracker.models.User
import com.example.familygpstracker.repositories.LocationRepository
import com.example.familygpstracker.repositories.NotificationRepository
import com.example.familygpstracker.repositories.ParentRepository
import com.example.familygpstracker.repositories.UserRepository
import com.example.familygpstracker.utility.NetworkUtils
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.utility.SharedPrefUtility
import kotlinx.coroutines.launch

class MainViewModel (
    private val userRepository: UserRepository ,
    private val parentRepository: ParentRepository ,
    private val notificationRepository: NotificationRepository,
    private val locationRepository: LocationRepository,
    private val context: Context
    ) : ViewModel(){

    private var sessionManager : SessionManager
    private var sharedPrefUtility: SharedPrefUtility
    init {
        sessionManager = SessionManager(context)
        sharedPrefUtility = SharedPrefUtility(context)

        viewModelScope.launch {

            if(NetworkUtils.haveNetworkConnection(context) ==true ){
                userRepository.getUser("jawad@gmail.com")
                parentRepository.getParentDetails("B91DB7C9-4032-4847-91D3-9491789AF1F0")
                notificationRepository.getAllNotifications()
                locationRepository.getLastLocation("2C1852C3-07F6-4976-98AA-38DFF2C550CF")
                locationRepository.getLastTenLocations("2C1852C3-07F6-4976-98AA-38DFF2C550CF")
            }
            else{
                Log.d("TAG", ": ")
            }

        }
    }

    val user : LiveData<User>
    get() = userRepository.user

    val parentDetail : LiveData<ParentDetail>
        get() = parentRepository.parentDetail

    val notificationList: LiveData<List<Notification>>
        get() = notificationRepository.notifications

    val location:LiveData<Location>
    get() = locationRepository.location

    val lastTenLocations : LiveData<List<Location>>
        get() = locationRepository.lastTenLocations

    suspend fun getLastLocation() {
        if(NetworkUtils.haveNetworkConnection(context)==true) {
            locationRepository.getLastLocation("2C1852C3-07F6-4976-98AA-38DFF2C550CF")
        }
    }

    suspend fun getLastTenLocations() {
        if(NetworkUtils.haveNetworkConnection(context)==true) {
            locationRepository.getLastTenLocations("2C1852C3-07F6-4976-98AA-38DFF2C550CF")
        }
    }

}
