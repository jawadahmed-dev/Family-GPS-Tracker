package com.example.familygpstracker.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familygpstracker.apis.UserService
import com.example.familygpstracker.models.User

class UserRepository (private val userService: UserService ) {

    private var userLiveData = MutableLiveData<User>()

    val user : LiveData<User>
    get() = userLiveData

    suspend fun getUser(email:String){
        var result = userService.getUserDetails(email)
        if(result?.body() != null){
            userLiveData.postValue(result.body())
        }
    }
}