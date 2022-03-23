package com.example.familygpstracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.familygpstracker.apis.ChildService
import com.example.familygpstracker.apis.NotificationService
import com.example.familygpstracker.apis.ParentService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.ActivityChildBinding
import com.example.familygpstracker.models.ChildDetail
import com.example.familygpstracker.models.NotificationDto
import com.example.familygpstracker.utility.SessionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChildActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    lateinit var binding : ActivityChildBinding
    lateinit var childDetail: ChildDetail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDataMembers()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("Token", token)
            // store token in shared prefferences
            updateDeviceToken()
            SessionManager(this).storeToken(token)
        })
        getChildDetail()
        binding.helpAlertBtn.setOnClickListener({
            sendNotification()
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
                        this@ChildActivity, "Token Updated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ChildActivity, "Token couldnt be Updated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initDataMembers() {
        sessionManager = SessionManager(this)
    }

    private fun getChildDetail() {
        var childService = RetrofitHelper.getInstance().create(ChildService::class.java)
        GlobalScope.launch {
           var result = childService.getChildDetail("DC293BDD-688C-4285-92D7-C589667A817A")
            if(result!=null && result.code() == 200){

                childDetail = result.body()!!

                if(childDetail?.parent != null ){
                    sessionManager.storeChildParentId(childDetail?.parent.parentId)
                }


            }
            else {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                       this@ChildActivity, "something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun sendNotification() {
        var notificationService = RetrofitHelper.getInstance().create(NotificationService::class.java)
        GlobalScope.launch {
            var result = notificationService.sendNotification(NotificationDto("i need help",
                true,"5DA1456F-6EB6-4A32-ABBE-6FAE1E035C09",
                "DC293BDD-688C-4285-92D7-C589667A817A","Help Alert"))
            if(result!=null && result.code() == 200){
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ChildActivity, result.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ChildActivity, "something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}