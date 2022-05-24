package com.example.familygpstracker.services

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.annotation.SuppressLint
import android.app.*
import android.util.Log
import com.google.android.gms.location.*

import android.location.Location
import android.os.*
import com.example.familygpstracker.apis.LocationService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.models.LocationDto
import com.example.familygpstracker.utility.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Looper

import android.os.HandlerThread
import com.example.familygpstracker.utility.SessionManager
import java.lang.Exception


class MyBackgroundLocationService : Service() {
    lateinit var locationClient : FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    lateinit var sessionManager: SessionManager


    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager(this)
        locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationCallback =   object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                if(p0.locations != null){

                    /*  Toast.makeText(applicationContext,"Location " + p0.lastLocation.latitude.toString(),
                          Toast.LENGTH_LONG).show()*/
                    var location : Location = p0.lastLocation
                    var locationService = RetrofitHelper.buildRetrofit().create(LocationService::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        if(NetworkUtils.isNetworkAvailable(this@MyBackgroundLocationService) == true){
                            try{
                                locationService.postLocation(sessionManager.getChildId().toString(),LocationDto(location.latitude,location.longitude))
                            }
                            catch(exception : Exception){

                                Log.d("ExceptionMessage", "ExceptionMessage: "+ exception.message)
                            }

                        }

                    }
                }
            }
        }
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    /*override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BackgroundService", "onStartCommand: ")

        getLocationUpdates()
        return START_STICKY
    }*/

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        getLocationUpdates()

        val CHANNEL_ID = "Foreground Service Id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_LOW
                )
            getSystemService(NotificationManager::class.java).createNotificationChannel(notificationChannel)
            var notification = Notification.Builder(this,CHANNEL_ID)
                .setContentTitle("Location Services Enabled")
                .setContentText("We will continuously get your location updates")
                .setSmallIcon(com.example.familygpstracker.R.drawable.ic_app_logo)
            startForeground(1001,notification.build())
        }

        return START_STICKY
    }


    private fun checkLocationPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

    @SuppressLint("MissingPermission")
    private fun getLocationUpdates() {
        var locationRequest : LocationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(2000)
        locationRequest.setFastestInterval(2000)

        val handlerThread = HandlerThread("MyHandlerThread")
        handlerThread.start()
        Thread.sleep(1000)
        if(checkLocationPermission()){
            locationClient.requestLocationUpdates(locationRequest,
              locationCallback,  Looper.getMainLooper())
        }
    }

   /* override fun onTaskRemoved(rootIntent: Intent?) {
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
    }*/


    override fun onDestroy() {
        Log.d("ds", "onDestroy: ")
    /*    val restartService = Intent(applicationContext, this.javaClass)
        val pendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartService,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME, 5000] = pendingIntent
        super.onDestroy()*/
     //   sendBroadcast(Intent("YouWillNeverKillMe"));
    }
}