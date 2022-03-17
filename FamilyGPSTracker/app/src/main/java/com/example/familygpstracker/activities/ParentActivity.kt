package com.example.familygpstracker.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.ParentService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.apis.UserService
import com.example.familygpstracker.databinding.ActivityMainBinding
import com.example.familygpstracker.repositories.ParentRepository
import com.example.familygpstracker.repositories.UserRepository
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory

class ParentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var navController:NavController? = null
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

       // setUpNavGraph()

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
                R.id.liveTrackFragment,
                R.id.locationHistoryFragment,
                R.id.notificationsFragment,
                R.id.menuFragment
            )
        )
    }

    private fun setUpViewModel() {

        var parentService = RetrofitHelper.getInstance().create(ParentService::class.java)
        var userService = RetrofitHelper.getInstance().create(UserService::class.java)
        var userRepository = UserRepository(userService)
        var parentRepository = ParentRepository(parentService)
        mainViewModel = ViewModelProvider(this,MainViewModelFactory(userRepository,parentRepository,this)).get(MainViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}