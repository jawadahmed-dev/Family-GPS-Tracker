package com.example.familygpstracker.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.*
import com.example.familygpstracker.databinding.ActivityMainBinding
import com.example.familygpstracker.repositories.LocationRepository
import com.example.familygpstracker.repositories.NotificationRepository
import com.example.familygpstracker.repositories.ParentRepository
import com.example.familygpstracker.repositories.UserRepository
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ParentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var navController:NavController? = null
    private lateinit var sessionManager : SessionManager
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        LinkBottonNavWithNavController();

        LinkActionBarWithNavController()

        setUpStatusBarColor()

        // this method instantiates UserService ,repository and initializes viewmodel
        setUpViewModel()

        setUpNavGraph()

        storeFCMToken()

    }

    private fun setUpNavGraph() {
        var parentDetail = mainViewModel.parentDetail.value
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment?
        navController = navHostFragment?.navController
        if(parentDetail?.children?.size == 0){
             navController?.setGraph(R.navigation.parent_link_device_nav_graph)
        }
        else {
            navController?.setGraph(R.navigation.main_nav_graph)
        }
    }

    private fun setUpStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.light_cyan))
        }
    }

    private fun LinkActionBarWithNavController() {
        setupActionBarWithNavController(navController!!,appBarConfiguration)
    }

    private fun LinkBottonNavWithNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavBar.setupWithNavController(navController!!)

        appBarConfiguration=AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.locationHistoryFragment,
                R.id.notificationsFragment,
                R.id.menuFragment
            )
        )
    }

    private fun storeFCMToken() {
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
    }

    private fun updateDeviceToken() {
        sessionManager = SessionManager(this)
        var parentService = RetrofitHelper.getInstance().create(ParentService::class.java)
        GlobalScope.launch {
            var result = parentService.updateDeviceToken(sessionManager.getParentId().toString(),
                sessionManager.getDeviceToken().toString())
            if(result != null && result.code() == 200){
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ParentActivity, "Token Updated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ParentActivity, "Token couldnt be Updated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setUpViewModel() {

        var parentService = RetrofitHelper.getInstance().create(ParentService::class.java)
        var userService = RetrofitHelper.getInstance().create(UserService::class.java)
        var notificationService = RetrofitHelper.getInstance().create(NotificationService::class.java)
        var locationService = RetrofitHelper.getInstance().create(LocationService::class.java)
        var userRepository = UserRepository(userService)
        var parentRepository = ParentRepository(parentService)
        var notificationRepository = NotificationRepository(notificationService)
        var locationRepository = LocationRepository(locationService)
        mainViewModel = ViewModelProvider(this,MainViewModelFactory(userRepository,parentRepository,notificationRepository,locationRepository,this)).get(MainViewModel::class.java)

    }

}