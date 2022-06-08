package com.example.familygpstracker.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.familygpstracker.R
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.apis.LocationService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.FragmentEnterCodeBinding
import com.example.familygpstracker.databinding.FragmentGeofenceBinding
import com.example.familygpstracker.models.CreateGeofence
import com.example.familygpstracker.models.LocationDto
import com.example.familygpstracker.services.MyBackgroundLocationService
import com.example.familygpstracker.utility.GeofenceUtility
import com.example.familygpstracker.utility.NetworkUtils
import com.example.familygpstracker.viewmodels.MainViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class GeofenceFragment : Fragment() {

    private lateinit var binding: FragmentGeofenceBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var locationClient : FusedLocationProviderClient
    private lateinit var currentLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGeofenceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeSureLocationAvailability()
        initDataMembers()
        registerListeners()
    }

    private fun makeSureLocationAvailability() {
        if(!checkLocationPermission()){
            requestLocationAccessPermission()
        }
    }

    private fun requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION ),1011)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1011 && !checkLocationPermission()){
            Toast.makeText(requireActivity(),"location permission is required for this operation!",Toast.LENGTH_LONG)
                .show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    companion object {
        fun newInstance() = GeofenceFragment();
    }


    private fun initDataMembers() {

        mainViewModel = (requireActivity() as ParentActivity).mainViewModel
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    private fun checkLocationPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

    private fun registerListeners() {

        binding.AddGeofenceBtn.setOnClickListener({

            if(binding.radiusEditText.text == null){
                return@setOnClickListener
            }

            var radius = binding.radiusEditText.text.toString().toDouble()
            var position = mainViewModel.lastIndex.value
            var selectedChildId = mainViewModel.selectedChildId.value.toString()



            if(currentLocation != null && selectedChildId != null){

                var createGeofence = makePayload(radius)

                if (radius != null && position!= null && position > -1 ){

                    CoroutineScope(Dispatchers.IO).launch {
                        if(NetworkUtils.isNetworkAvailable(requireActivity()) == true){
                            try{
                               var isCreated =  mainViewModel.createGeofence(selectedChildId,createGeofence)

                                withContext(Dispatchers.Main) {
                                    if (isCreated) {
                                        Toast.makeText(
                                            requireActivity(),
                                            "Geofence added",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireActivity(),
                                            "Something went wrong",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                            }
                            catch(exception : Exception){

                                Log.d("ExceptionMessage", "ExceptionMessage: "+ exception.message)
                            }

                        }

                    }

                }
                else {
                    Toast.makeText(requireActivity(),"Select Child First!",Toast.LENGTH_SHORT).show()
                }
            }

        })

        if(checkLocationPermission()){

            locationClient.lastLocation.addOnSuccessListener {
                currentLocation = it
            }
        }

    }

    private fun makePayload(radius : Double): CreateGeofence {
        return CreateGeofence(currentLocation.latitude,currentLocation.longitude,radius,(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT).toString())
    }

    private fun getGeofenceList(){


    }

}