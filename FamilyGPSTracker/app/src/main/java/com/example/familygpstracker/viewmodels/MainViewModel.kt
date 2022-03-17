package com.example.familygpstracker.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familygpstracker.models.ParentDetail
import com.example.familygpstracker.models.User
import com.example.familygpstracker.repositories.ParentRepository
import com.example.familygpstracker.repositories.UserRepository
import com.example.familygpstracker.utility.SessionManager
import kotlinx.coroutines.launch

class MainViewModel (
    private val userRepository: UserRepository ,
    private val parentRepository: ParentRepository ,
    private val context: Context
    ) : ViewModel(){

    private lateinit var sessionManager : SessionManager
    init {
        sessionManager = SessionManager(context)
        viewModelScope.launch {
            userRepository.getUser("jawad@gmail.com")
            parentRepository.getParentDetails("B91DB7C9-4032-4847-91D3-9491789AF1F0")
        }
    }

    val user : LiveData<User>
    get() = userRepository.user

    val parentDetail : LiveData<ParentDetail>
        get() = parentRepository.parentDetail
}