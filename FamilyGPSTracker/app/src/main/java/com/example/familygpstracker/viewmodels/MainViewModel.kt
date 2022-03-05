package com.example.familygpstracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familygpstracker.models.User
import com.example.familygpstracker.repositories.UserRepository
import kotlinx.coroutines.launch

class MainViewModel (private val repository: UserRepository) : ViewModel(){

    init {
        viewModelScope.launch {
            repository.getUser("jawad@gmail.com")
        }
    }

    val user : LiveData<User>
    get() = repository.user
}