package com.example.familygpstracker.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.apis.UserService
import com.example.familygpstracker.databinding.ActivityMainBinding
import com.example.familygpstracker.models.User
import com.example.familygpstracker.repositories.UserRepository
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var controller:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavBar.setupWithNavController(navController)
        //binding.fragmentContainerView.findNavController()
        appBarConfiguration=AppBarConfiguration(
            setOf(
                R.id.liveTrackFragment,
                R.id.locationHistoryFragment,
                R.id.notificationsFragment,
                R.id.menuFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.light_cyan))
        }

        /*GlobalScope.launch {
            var result = RetrofitHelper.getInstance().create(UserService::class.java).getUserDetails("CBC4AE1A-56A0-4A8A-97C1-48404548F02C")
            if(result != null){
                var user = result.body()
                Log.d("TAG",(user as User).toString())
            }
            else{
                Log.d("TAG",""+ result.message())
            }
        }*/

        // this method instantiates UserService ,repository and initializes viewmodel
        setUpViewModel()

    }

    private fun setUpViewModel() {

        var userService = RetrofitHelper.getInstance().create(UserService::class.java)
        var repository = UserRepository(userService)
        mainViewModel = ViewModelProvider(this,MainViewModelFactory(repository)).get(MainViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}