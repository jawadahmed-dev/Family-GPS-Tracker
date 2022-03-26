package com.example.familygpstracker.services

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.R
import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import com.google.android.gms.location.*
import android.os.SystemClock.elapsedRealtime

import android.app.AlarmManager

import android.app.PendingIntent
import android.os.SystemClock


class MyBackgroundLocationService : Service() {
    lateinit var locationClient : FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BackgroundService", "onStartCommand: ")

        getLocationUpdates()
        return START_STICKY_COMPATIBILITY
    }

    private fun checkLocationPermission() : Boolean{
        return  ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

    @SuppressLint("MissingPermission")
    private fun getLocationUpdates() {
        var locationRequest : LocationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(5000)
        locationRequest.setFastestInterval(2000)

        if(checkLocationPermission()){
            locationClient.requestLocationUpdates(locationRequest,
                object : LocationCallback(){
                    override fun onLocationResult(p0: LocationResult) {
                        super.onLocationResult(p0)
                        if(p0.locations != null){

                              /*  Toast.makeText(applicationContext,"Location " + p0.lastLocation.latitude.toString(),
                                    Toast.LENGTH_LONG).show()*/

                        }
                    }
                }, Looper.getMainLooper())
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)


        val restartServicePendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmService = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] =
            restartServicePendingIntent

        super.onTaskRemoved(rootIntent)
    }


    override fun onDestroy() {
        val restartService = Intent(applicationContext, this.javaClass)
        val pendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartService,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME, 5000] = pendingIntent
        super.onDestroy()
     //   sendBroadcast(Intent("YouWillNeverKillMe"));
    }
}