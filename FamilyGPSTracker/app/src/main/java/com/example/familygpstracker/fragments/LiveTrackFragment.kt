package com.example.familygpstracker.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.familygpstracker.R
import com.example.familygpstracker.databinding.FragmentLiveTrackBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.utility.GeofenceUtility
import com.example.familygpstracker.viewmodels.MainViewModel
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions
import kotlinx.coroutines.*
import com.google.android.gms.maps.model.Marker
import java.util.*


class LiveTrackFragment : Fragment() , OnMapReadyCallback {

    private lateinit var mHandler: Handler
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var mRunnable: Runnable
    private lateinit var map : SupportMapFragment
    private lateinit var googleMap: GoogleMap
    //private var isMapActive = false
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentLiveTrackBinding
    private val FINE_BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 1011

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.fragment_live_track,container,false);
        // Inflate the layout for this fragment
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMember()
        registerListeners()
        syncMap()
    }

    private fun registerListeners() {
        binding.swiperefresh.setOnRefreshListener({
            if(mainViewModel.selectedChildId.value == null){
                binding.swiperefresh.isRefreshing = false

            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getGeofenceList(mainViewModel.selectedChildId.value!!)
                }
            }

        })
    }

    private fun goToLocation(latLang: LatLng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLang))
    }


    private fun registerObserver() {

        mainViewModel.location.observe(requireActivity(),{
            var latLang = LatLng(it.latitude.toDouble(),it.longitude.toDouble())

            if(getActivity() != null && googleMap!=null){
                googleMap.clear();
                moveMarker(latLang)
                goToLocation(latLang)
            }


        })

        mainViewModel.geofenceList.observe(requireActivity(),{
            for(geofence in it){
                var latLang = LatLng(geofence.latitude,geofence.longitude)
                var radius = geofence.radius
              //  addGeofence(latLang,radius)
                addCircle(latLang,radius)
            }
            binding.swiperefresh.isRefreshing = false
        })
        /*mainViewModel.lastIndex.observe(requireActivity(),{
            if(it > -1){
                isMapActive =false
                if(googleMap!=null){
                    startUpdatingMap(it,1500L)
                }

            }


        })*/
    }

    private fun moveMarker(latLang: LatLng) {

         val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        var address = geocoder.getFromLocation(latLang.latitude,latLang.longitude,3).get(2)
        val marker: Marker? = googleMap.addMarker(MarkerOptions().position(latLang).title(
            address.getAddressLine(address.maxAddressLineIndex)))
        marker?.showInfoWindow()
    }

    private fun syncMap() {
        map.getMapAsync(this)
    }

    private fun initDataMember() {
        mHandlerThread = HandlerThread("HandlerThread");
        mHandlerThread.start();
        mHandler = Handler(mHandlerThread.getLooper());
        mainViewModel = (requireActivity() as ParentActivity).mainViewModel
        map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //geofencingClient = LocationServices.getGeofencingClient(requireActivity())
        //geofencingUtility = GeofenceUtility(requireActivity())
    }

   /* private fun addGeofence(latLang: LatLng, radius: Double) : Unit{
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
                addCircle(latLang,radius)
            }
            .addOnFailureListener {
               var errorMessage =  geofencingUtility.getErrorString(it)
                Log.d("Geofence", "geofenceFailure : "+errorMessage)
            }
    }
*/
    private fun addCircle(latLng : LatLng, radius: Double) : Unit{
        var circleOptions = CircleOptions()
        circleOptions.center(latLng)
        circleOptions.radius(radius)
        circleOptions.strokeColor(Color.argb(255,255,0,0))
        circleOptions.fillColor(Color.argb(64,255,0,0))
        circleOptions.strokeWidth(4f)
        googleMap.addCircle(circleOptions)

    }

    private fun startUpdatingMap(timeInterval: Long) {
       // isMapActive = true
        var position = mainViewModel.lastIndex.value
       // if(position!! > -1){
            /*return CoroutineScope(Dispatchers.IO).launch {
                while (isMapActive) {
                    // add your task here
                    delay(timeInterval)
                    if(position > -1){
                        mainViewModel.getLastLocation(mainViewModel.selectedChildId.toString())
                    }

                }
            }*/
            mRunnable = object: Runnable {
                override fun run() {
                    if(position != null && position > -1){
                        CoroutineScope(Dispatchers.IO).launch {
                            mainViewModel.getLastLocation(mainViewModel.selectedChildId.toString())
                        }
                    }
                    mHandler.postDelayed(this, timeInterval)
                }
            }

            mHandler.post({ mHandler.post(mRunnable)})

       // }

    }

    companion object {
        fun newInstance() = LiveTrackFragment();
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled =true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        var rawalpindiCBlock: LatLng = LatLng(33.632687, 73.076726)
        p0.addMarker(MarkerOptions().position(rawalpindiCBlock).title("Rawalpindi C-Block"))
        p0.moveCamera(CameraUpdateFactory.newLatLng(rawalpindiCBlock))
        registerObserver()
        startUpdatingMap(3500L)

    }

    private fun requestPermissionLocation(){
        if(!checkLocationPermission()){
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION )
                ,FINE_BACKGROUND_LOCATION_ACCESS_REQUEST_CODE)
        }
    }
    private fun checkLocationPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == FINE_BACKGROUND_LOCATION_ACCESS_REQUEST_CODE && !checkLocationPermission()){

          Toast.makeText(requireActivity(),"Background permission required!",Toast.LENGTH_LONG)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        //isMapActive = false
        mHandler.removeCallbacks(mRunnable)
        super.onPause()
    }

    override fun onStop() {

        super.onStop()
    }

    override fun onDestroy() {
        mHandlerThread.quitSafely()
        super.onDestroy()
    }

    override fun onResume() {
        startUpdatingMap(3500L)
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}