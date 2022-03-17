package com.example.familygpstracker.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {



    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("Token", "onNewToken: "+ p0)
    }
}