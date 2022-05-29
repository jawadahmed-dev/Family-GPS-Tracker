package com.example.familygpstracker.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.*
import com.example.familygpstracker.databinding.ActivityParentLinkDeviceBinding
import com.example.familygpstracker.repositories.*
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LinkDeviceActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var mainViewModel: MainViewModel
    private var navController:NavController? = null
    private lateinit var binding : ActivityParentLinkDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataMembers()
        changeStatusBarColor()
        var key = intent.getIntExtra("key",1)
        //updateDeviceToken()
       // storeFCMToken()
        setUpViewModel()


        mainViewModel.parentDetail.observe(this, {
            binding.progressBar.visibility = View.INVISIBLE
            if(hasParentHaveChild() && key == 1){
                startActivity(Intent(this,ParentActivity::class.java))
                finish()
            }
            else {
                navController?.setGraph(R.navigation.parent_link_device_nav_graph)
                navController?.navigate(R.id.connectDeviceFragment)
            }
        })


    }

   /* private fun storeFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("MyToken", token)
            // store token in shared prefferences
            SessionManager(this).storeToken(token)
        })
    }*/

    private fun updateDeviceToken() {
        var parentService = RetrofitHelper.buildRetrofit().create(ParentService::class.java)
        GlobalScope.launch {
            var result = parentService.updateDeviceToken(sessionManager.getParentId().toString(),
                sessionManager.getDeviceToken().toString())
            if(result != null && result.code() == 200){
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ParentLinkDeviceActivity, "Token Updated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ParentLinkDeviceActivity, "Token couldnt be Updated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initDataMembers() {
        sessionManager = SessionManager(this)
        binding = ActivityParentLinkDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.parentLinkDeviceFragmentContainerView) as NavHostFragment?
        navController = navHostFragment?.navController
    }


    private fun hasParentHaveChild(): Boolean {
        var parentDetail = mainViewModel.parentDetail.value
        if(parentDetail?.children?.size == 0){
            return false;
        }
        return true;
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
        mainViewModel = ViewModelProvider(this,MainViewModelFactory(userRepository,parentRepository,notificationRepository,locationRepository,geofenceRepository,this)).get(MainViewModel::class.java)

    }
    fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }
}