package com.example.familygpstracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.familygpstracker.R
import com.example.familygpstracker.utility.SessionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var sessionManager : SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        initDataMembers()
        storeFCMToken()
        checkSessionAndNavigate()

    }

    private fun initDataMembers() {
        sessionManager = SessionManager(this)
    }

    private fun storeFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("Token", token)
            // store token in shared prefferences
            SessionManager(this).storeToken(token)
        })
    }

    private fun checkSessionAndNavigate() {

        if(sessionManager.isLoggedIn()){
            when(sessionManager.getUserType()){
                "parent" -> startActivity(Intent(this,ParentLinkDeviceActivity::class.java))
                "child" -> startActivity(Intent(this,ChildLinkDeviceActivity::class.java))
                 null -> return
            }
        }
    }
}