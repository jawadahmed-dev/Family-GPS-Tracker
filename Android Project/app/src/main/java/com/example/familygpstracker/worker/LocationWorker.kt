package com.example.familygpstracker.worker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters

import com.example.familygpstracker.apis.LocationService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.models.LocationDto
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.*


class LocationWorker (private val context:Context , params : WorkerParameters)
    : Worker(context , params){

    private lateinit var locationClient : FusedLocationProviderClient
    private lateinit var location : Location

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {

        initDataMembers()
        var locationService = RetrofitHelper.buildRetrofit().create(LocationService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..450){
               /* if(NetworkUtils.isNetworkAvailable(context) == false) {
                    break
                }*/
                locationService.postLocation("2C1852C3-07F6-4976-98AA-38DFF2C550CF",
                    LocationDto(12.322323,63.324234)
                )
                delay(2000)

               /* locationClient.lastLocation.addOnCompleteListener({
                if(it.isSuccessful){
                    location = it.result
                }
                    //Log.d("MyWorker", "doWork: ")
                })*/
               /* withContext(Dispatchers.Main){
                    Toast.makeText(context,"hello",Toast.LENGTH_LONG)
                }
*/
               // checkLocationPermission()
            }

        }

        return Result.success()
    }
    private fun initDataMembers(){
        locationClient = FusedLocationProviderClient(context)
    }

/*
    private fun getCurrentLocation(){

        if(LocationUtility.isLocationEnabled(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ){


            }
            else {

                locationClient.lastLocation.addOnCompleteListener({
                    Toast.makeText(
                        context, it.getResult().latitude.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                })

            }

        }

    }*/




}