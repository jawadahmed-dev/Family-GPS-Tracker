package com.example.familygpstracker.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.familygpstracker.activities.ChildActivity
import com.example.familygpstracker.apis.NotificationService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.FragmentChildScreenBinding
import com.example.familygpstracker.models.ChildDetail
import com.example.familygpstracker.models.NotificationDto
import com.example.familygpstracker.services.MyBackgroundLocationService
import com.example.familygpstracker.utility.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChildScreenFragment : Fragment() {

    private lateinit var binding : FragmentChildScreenBinding
    private lateinit var childDetail: ChildDetail
    private lateinit var activity: ChildActivity
    private val requestMultiplePermissions  = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        if(checkLocationPermission()){
            startBackgroundService()
        }
        else{
            Toast.makeText(requireActivity(),"Need to allow location access all the time!",Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildScreenBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMembers()
        bindDataToView()
        registerListeners()

        if(checkLocationPermission()){
            startBackgroundService()
        }
        else{
            requestPermissionLocation()
        }


    }

    private fun registerListeners() {
        binding.helpAlertBtn.setOnClickListener({
            sendNotification()
        })
    }

    private fun startBackgroundService() {
        var intent = Intent(requireActivity(), MyBackgroundLocationService::class.java)
        intent.putExtra("UserID", "123456");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(checkLocationPermission()){
                requireActivity().startForegroundService(intent)
            }

        }
    }

    private fun bindDataToView() {
        binding.trackerName.text = childDetail.parent.name
        binding.trackerEmail.text = childDetail.parent.email
        binding.trackerPhoneNumber.text = childDetail.parent.phoneNumber
    }

    private fun initDataMembers() {
        childDetail = ( requireActivity() as ChildActivity ).childDetail
        activity = requireActivity() as ChildActivity
    }

    private fun requestPermissionLocation(){

            /*ActivityCompat.requestPermissions(requireActivity(),arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION ,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION ),1011)*/

        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION ,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION ))

    }

    private fun checkLocationPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun sendNotification() {

        var notificationService = RetrofitHelper.buildRetrofit().create(NotificationService::class.java)
        if(NetworkUtils.haveNetworkConnection(requireActivity())) {
            GlobalScope.launch {
                var result = notificationService.sendNotification(
                    NotificationDto(
                        "i need help",
                        true, activity.sessionManager.getChildParentId().toString(),
                        activity.sessionManager.getChildId().toString(), activity.sessionManager.getChildName().toString()
                    )
                )
                if (result != null && result.code() == 200) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(), result.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(), "something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        else{
            Toast.makeText(
                activity, "No Internet Available!",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1011 && checkLocationPermission()){
            startBackgroundService()
        }
        else{
            requestPermissionLocation()
        }
    }
}