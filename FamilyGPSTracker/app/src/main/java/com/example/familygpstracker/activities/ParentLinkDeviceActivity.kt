package com.example.familygpstracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory

class ParentLinkDeviceActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    private var navController:NavController? = null
    private lateinit var binding : ActivityParentLinkDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataMembers()
        setUpViewModel()

        mainViewModel.parentDetail.observe(this, {
            if(!hasParentHaveNoChild()){
                startActivity(Intent(this,ParentActivity::class.java))
                finish()
            }
            else {
                navController?.navigate(R.id.connectDeviceFragment)
            }
        })


    }

    private fun initDataMembers() {
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
}