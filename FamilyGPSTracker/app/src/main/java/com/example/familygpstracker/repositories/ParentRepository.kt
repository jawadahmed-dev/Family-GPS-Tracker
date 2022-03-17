package com.example.familygpstracker.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familygpstracker.apis.ParentService
import com.example.familygpstracker.apis.UserService
import com.example.familygpstracker.models.ParentDetail
import com.example.familygpstracker.models.User

class ParentRepository (private val parentService: ParentService) {

    private var parentDetailLiveData = MutableLiveData<ParentDetail>()

    val parentDetail : LiveData<ParentDetail>
        get() = parentDetailLiveData

    suspend fun getParentDetails(id:String){
        var result = parentService.getParentDetails(id)
        if(result?.body() != null){
            parentDetailLiveData.postValue(result.body())
        }
    }
}