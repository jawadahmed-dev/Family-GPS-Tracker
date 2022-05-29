package com.example.familygpstracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.*
import com.example.familygpstracker.repositories.*
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.viewmodels.AuthenticationViewModel
import com.example.familygpstracker.viewmodels.AuthenticationViewModelFactory
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var sessionManager : SessionManager
    lateinit var authViewModel : AuthenticationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        initDataMembers()
        storeFCMToken()
        checkSessionAndNavigate()

    }

    private fun initDataMembers() {
        sessionManager = SessionManager(this)
        setUpViewModel()
    }

    private fun storeFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("Token", token)
            // store token in shared prefferences
            SessionManager(this).storeToken(token)
        })
    }

    private fun checkSessionAndNavigate() {

        if(sessionManager.isLoggedIn()){
            when(sessionManager.getUserType()){
                "parent" -> startActivity(Intent(this,LinkDeviceActivity::class.java))
                "child" -> startActivity(Intent(this,ChildActivity::class.java))
                 null -> return
            }
        }
    }

    private fun setUpViewModel() {

        var parentService = RetrofitHelper.buildRetrofit().create(ParentService::class.java)
        var userService = RetrofitHelper.buildRetrofit().create(UserService::class.java)
        var notificationService = RetrofitHelper.buildRetrofit().create(NotificationService::class.java)
        var locationService = RetrofitHelper.buildRetrofit().create(LocationService::class.java)
        var geofenceService = RetrofitHelper.buildRetrofit().create(GeofenceService::class.java)
        var userRepository = UserRepository(userService)
        var parentRepository = ParentRepository(parentService)
        var notificationRepository = NotificationRepository(notificationService)
        var locationRepository = LocationRepository(locationService)
        var geofenceRepository = GeofenceRepository(geofenceService)

        authViewModel = ViewModelProvider(this,
            AuthenticationViewModelFactory(userRepository,parentRepository,notificationRepository,locationRepository,geofenceRepository,this)
        ).get(AuthenticationViewModel::class.java)

    }
}