package com.example.familygpstracker.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familygpstracker.apis.UserService
import com.example.familygpstracker.models.User
import retrofit2.Response

class UserRepository (private val userService: UserService) {

    private var userLoginResultLiveData = MutableLiveData<Response<User>>()

    val userLoginResult : LiveData<Response<User>>
    get() = userLoginResultLiveData

    suspend fun getUserLoginResult(email:String){

        var result = userService.getUserDetails(email)
        userLoginResultLiveData.postValue(result)

    }
}