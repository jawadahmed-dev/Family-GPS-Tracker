package com.example.familygpstracker.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.familygpstracker.apis.ChildService
import com.example.familygpstracker.apis.NotificationService
import com.example.familygpstracker.apis.ParentService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.broadcastreceiver.RestartServiceReceiver
import com.example.familygpstracker.databinding.ActivityChildBinding
import com.example.familygpstracker.models.ChildDetail
import com.example.familygpstracker.models.NotificationDto
import com.example.familygpstracker.services.MyBackgroundLocationService
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.worker.LocationWorker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class ChildActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    lateinit var binding : ActivityChildBinding
    lateinit var childDetail: ChildDetail
    lateinit var locationClient : FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDataMembers()
       // checkLocationPermission()
       // getLocation()
       // getLocationUpdate()

        setUpWorker()

       /* var intent = Intent(this,MyBackgroundLocationService::class.java)
        intent.putExtra("UserID", "123456");
        startService(intent)*/
     /*   val br: BroadcastReceiver = RestartServiceReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction("YouWillNeverKillMe")
        }
        registerReceiver(br,filter)
        sendBroadcast(Intent("YouWillNeverKillMe"));*/


       /* FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
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
        })*/
    }

    private fun getLocation() {
        if(checkLocationPermission()){
            locationCallback = object : LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    if(p0.locations != null){
                        Toast.makeText(this@ChildActivity,"Location " + p0.lastLocation.latitude.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setUpWorker() {
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest = PeriodicWorkRequest.Builder(LocationWorker::class.java,5,TimeUnit.SECONDS)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance(this).enqueue(workerRequest)

    }

    private fun checkLocationPermission() : Boolean{
        return  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


        //

    }

    private fun isGPSEnabled() : Boolean{
        var locationManager : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        var providerEnabled : Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(providerEnabled){
            return true
        }
        else {
            AlertDialog.Builder(this)
                .setTitle("GPS Permissions")
                .setMessage("GPS is required for this app to work")
                .setPositiveButton("yes"){
                    dialog , which ->

                        var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(intent ,1001)
                }
                .setCancelable(false)
                .show()
        }
        return false
    }

    private fun requestPermissionLocation(){
        if(!checkLocationPermission()){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION ),1011)
        }
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
        locationClient = FusedLocationProviderClient(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1001 ){
            if(isGPSEnabled()){
                Toast.makeText(this,"GPS is enabled ",Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this,"GPS is not enabled ",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getLocationUpdate(){
        var locationRequest : LocationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(5000)
        locationRequest.setFastestInterval(2000)

        if(checkLocationPermission()){
            locationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
     //   sendBroadcast(Intent("YouWillNeverKillMe"));
    }
}