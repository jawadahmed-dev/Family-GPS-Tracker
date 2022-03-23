package com.example.familygpstracker.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.ParentService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.apis.UserService
import com.example.familygpstracker.databinding.ActivityParentLinkDeviceBinding
import com.example.familygpstracker.repositories.ParentRepository
import com.example.familygpstracker.repositories.UserRepository
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ParentLinkDeviceActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var mainViewModel: MainViewModel
    private var navController:NavController? = null
    private lateinit var binding : ActivityParentLinkDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataMembers()
        changeStatusBarColor()
        updateDeviceToken()
        setUpViewModel()


        mainViewModel.parentDetail.observe(this, {
            if(!hasParentHaveNoChild()){
                startActivity(Intent(this,ParentActivity::class.java))
                finish()
            }
            else {
                navController?.setGraph(R.navigation.parent_link_device_nav_graph)
                navController?.navigate(R.id.connectDeviceFragment)
            }
        })


    }

    private fun updateDeviceToken() {
        var parentService = RetrofitHelper.getInstance().create(ParentService::class.java)
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


    private fun hasParentHaveNoChild(): Boolean {
        var parentDetail = mainViewModel.parentDetail.value
        if(parentDetail?.children?.size == 0){
            return true;
        }
        return false;
    }

    private fun setUpViewModel() {

        var parentService = RetrofitHelper.getInstance().create(ParentService::class.java)
        var userService = RetrofitHelper.getInstance().create(UserService::class.java)
        var userRepository = UserRepository(userService)
        var parentRepository = ParentRepository(parentService)
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(userRepository,parentRepository,this)
        ).get(MainViewModel::class.java)

    }
    fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }
}