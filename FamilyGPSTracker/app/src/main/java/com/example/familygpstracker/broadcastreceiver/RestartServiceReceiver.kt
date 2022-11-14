package com.example.familygpstracker.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.familygpstracker.services.MyBackgroundLocationService;

 class RestartServiceReceiver : BroadcastReceiver() {
     override fun onReceive(p0: Context?, p1: Intent?) {
         p0?.startService(Intent(p0?.getApplicationContext(), MyBackgroundLocationService::class.java))
     }

 }
