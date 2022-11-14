package com.example.familygpstracker.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.ChildService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.ActivityChildBinding
import com.example.familygpstracker.models.ChildDetail
import com.example.familygpstracker.services.MyBackgroundLocationService
import com.example.familygpstracker.utility.GeofenceUtility
import com.example.familygpstracker.utility.NetworkUtils
import com.example.familygpstracker.utility.SessionManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChildActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    lateinit var binding : ActivityChildBinding
    lateinit var childDetail: ChildDetail
    private var navController: NavController? = null
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofencingUtility: GeofenceUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDataMembers()

        getChildDetail()
       // checkLocationPermission()
       // getLocation()
       // getLocationUpdate()

       // setUpWorker()



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

    private fun startFetchingLocation() {


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1011 && checkLocationPermission()){

                var intent = Intent(this,MyBackgroundLocationService::class.java)
                intent.putExtra("UserID", "123456");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

  /*  private fun getLocation() {
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
    }*/

   /* private fun setUpWorker() {
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest = PeriodicWorkRequest.Builder(LocationWorker::class.java,5,TimeUnit.SECONDS)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance(this).enqueue(workerRequest)

    }*/

    private fun checkLocationPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

 /*   private fun isGPSEnabled() : Boolean{
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
*/
    private fun requestPermissionLocation(){
        if(!checkLocationPermission()){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION ),1011)
        }
    }

    /*private fun updateDeviceToken() {
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
    }*/

    private fun initDataMembers() {
        sessionManager = SessionManager(this)
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.childFragmentContainerView) as NavHostFragment?
        navController = navHostFragment?.navController

    }




    private fun getChildDetail() {
        var childService = RetrofitHelper.buildRetrofit().create(ChildService::class.java)
        if(NetworkUtils.haveNetworkConnection(this)) {
            GlobalScope.launch {
                var result = childService.getChildDetail(sessionManager.getChildId().toString())
                if (result != null && result.code() == 200) {

                    childDetail = result.body()!!
                    binding.progressBar.visibility = View.INVISIBLE

                    if (childDetail?.parent != null) {

                        sessionManager.storeChildParentId(childDetail?.parent.parentId)
                        sessionManager.storeChildName(childDetail?.name)
                        withContext(Dispatchers.Main) {
                            navController?.setGraph(R.navigation.child_link_device_nav_graph)
                            navController?.navigate(R.id.childScreenFragment)
                        }

                        if(childDetail.geofences != null ){
                            for (geofence in childDetail?.geofences){
                                var latLang = LatLng(geofence.latitude,geofence.longitude)
                                addGeofence(latLang,geofence.radius)
                            }
                        }

                        /*bindData(childDetail)
                    startFetchingLocation()*/
                    }

                    else {

                        withContext(Dispatchers.Main) {
                            navController?.setGraph(R.navigation.child_link_device_nav_graph)
                            navController?.navigate(R.id.generateCodeFragment)
                        }


                    }


                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChildActivity, "something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        else{
            Toast.makeText(
                this, "No Internet Available!",
                Toast.LENGTH_LONG
            ).show()
        }

    }





   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1001 ){
            if(isGPSEnabled()){
                Toast.makeText(this,"GPS is enabled ",Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this,"GPS is not enabled ",Toast.LENGTH_LONG).show()
            }
        }
    }*/


    private fun addGeofence(latLang: LatLng, radius: Double) : Unit{
        var geofence = geofencingUtility.getGeofence("geofenceId", latLang, radius.toFloat(),
            Geofence.GEOFENCE_TRANSITION_ENTER or
                    Geofence.GEOFENCE_TRANSITION_EXIT)
        var geofencingRequest = geofencingUtility.getGeofencingRequest(geofence)
        var pendingIntent = geofencingUtility.getPendingIntent()
        if (!checkLocationPermission()) {
            requestPermissionLocation()
        }
        geofencingClient.addGeofences(geofencingRequest,pendingIntent)
            .addOnSuccessListener {
                Log.d("Geofence", "geofenceSuccess : geofence added")

            }
            .addOnFailureListener {
                var errorMessage =  geofencingUtility.getErrorString(it)
                Log.d("Geofence", "geofenceFailure : "+errorMessage)
            }
    }

}