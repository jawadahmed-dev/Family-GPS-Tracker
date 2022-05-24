package com.example.familygpstracker.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familygpstracker.models.*
import com.example.familygpstracker.repositories.*
import com.example.familygpstracker.utility.NetworkUtils
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.utility.SharedPrefUtility
import kotlinx.coroutines.launch

class AuthenticationViewModel (
    private val userRepository: UserRepository,
    private val parentRepository: ParentRepository,
    private val notificationRepository: NotificationRepository,
    private val locationRepository: LocationRepository,
    private val geofenceRepository: GeofenceRepository,
    private val context: Context
) : ViewModel(){

    private var sessionManager : SessionManager
    private var sharedPrefUtility: SharedPrefUtility
    private var lastIndexLiveData = MutableLiveData<Int>()
    private var selectedChildIdLiveData  = MutableLiveData<String?>()

    init {

        sessionManager = SessionManager(context)
        sharedPrefUtility = SharedPrefUtility(context)
        lastIndexLiveData.postValue(-1)
        selectedChildIdLiveData.postValue(null)

        viewModelScope.launch {
            try {
                if(NetworkUtils.haveNetworkConnection(context) ==true ){
                    // userRepository.getUser("jawad@gmail.com")
                    parentRepository.getParentDetails(sessionManager.getParentId().toString())
                    notificationRepository.getAllNotifications()
//                locationRepository.getLastLocation("2C1852C3-07F6-4976-98AA-38DFF2C550CF")
//                locationRepository.getLastTenLocations("2C1852C3-07F6-4976-98AA-38DFF2C550CF")
                }
                else{
                    Toast.makeText(context,"Network not available", Toast.LENGTH_LONG).show()
                }
            }
            catch(e:Exception){
                Toast.makeText(context,"Failed to Connect", Toast.LENGTH_LONG).show()
            }


        }
    }

    fun updatelastIndex(lastIndex : Int){
        lastIndexLiveData.postValue(lastIndex)
    }

    val geofenceList : LiveData<List<Geofence>>
        get() = geofenceRepository.geofenceList

    suspend fun getGeofenceList(childId : String) {
        if(NetworkUtils.haveNetworkConnection(context)==true) {
            geofenceRepository.getGeofenceList(childId)
        }
    }

    suspend fun createGeofence(childId : String, createGeofence: CreateGeofence) : Boolean {

        if(NetworkUtils.haveNetworkConnection(context)==true) {
            return geofenceRepository.createGeofence(childId,createGeofence)
        }

        return false

    }

    val lastIndex : LiveData<Int>
        get() = lastIndexLiveData

    fun updateSelectedChildId(childId: String?){
        selectedChildIdLiveData.postValue(childId)
    }

    val selectedChildId : LiveData<String?>
        get() = selectedChildIdLiveData

    val user : LiveData<User>
        get() = userRepository.user

    val parentDetail : LiveData<ParentDetail>
        get() = parentRepository.parentDetail

    val notificationList: LiveData<List<Notification>>
        get() = notificationRepository.notifications

    val location: LiveData<Location>
        get() = locationRepository.location

    val lastTenLocations : LiveData<List<Location>>
        get() = locationRepository.lastTenLocations

    suspend fun getLastLocation(childId : String) {
        if(NetworkUtils.haveNetworkConnection(context)==true) {
            locationRepository.getLastLocation(childId)
        }
    }

    suspend fun getLastTenLocations(childId : String) {
        if(NetworkUtils.haveNetworkConnection(context)==true) {
            locationRepository.getLastTenLocations(childId)
        }
    }

    suspend fun getALLNotifications() {
        if(NetworkUtils.haveNetworkConnection(context)==true) {
            notificationRepository.getAllNotifications()
        }
    }

}