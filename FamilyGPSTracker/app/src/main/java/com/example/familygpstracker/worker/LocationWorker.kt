package com.example.familygpstracker.worker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import android.net.Network
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.familygpstracker.utility.LocationUtility
import com.example.familygpstracker.utility.NetworkUtils
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.*


class LocationWorker (private val context:Context , params : WorkerParameters)
    : Worker(context , params){

    private lateinit var locationClient : FusedLocationProviderClient
    private lateinit var location : Location

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {

        initDataMembers()
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..450){
                if(NetworkUtils.isNetworkAvailable(context) == false) {
                    break
                }
                delay(2000)
               /* locationClient.lastLocation.addOnCompleteListener({
                if(it.isSuccessful){
                    location = it.result
                }
                    //Log.d("MyWorker", "doWork: ")
                })*/
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"hello",Toast.LENGTH_LONG)
                }

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