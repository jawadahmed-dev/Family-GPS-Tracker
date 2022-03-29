package com.example.familygpstracker.fragments

import android.location.Address
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
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.viewmodels.MainViewModel
import kotlinx.coroutines.*
import com.google.android.gms.maps.model.Marker
import java.util.*
import kotlin.math.log


class LiveTrackFragment : Fragment() , OnMapReadyCallback {

    private lateinit var mHandler: Handler
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var map : SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private var isMapActive = false
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentLiveTrackBinding

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
        syncMap()

    }

    private fun goToLocation(latLang: LatLng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLang))
    }


    private fun registerObserver() {

        mainViewModel.location.observe(requireActivity(),{
            var latLang = LatLng(it.latitude.toDouble(),it.longitude.toDouble())

            if(getActivity() != null){
                moveMarker(latLang)
                goToLocation(latLang)
            }


        })
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
    }



    private fun startUpdatingMap(timeInterval: Long): Job {
        isMapActive = true
        return CoroutineScope(Dispatchers.IO).launch {
            while (isMapActive) {
                // add your task here
                delay(timeInterval)
                mainViewModel.getLastLocation()

            }
        }
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
        startUpdatingMap(4000L)
    }

    override fun onPause() {
        isMapActive = false
        super.onPause()
    }

    override fun onStop() {

        super.onStop()
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    override fun onResume() {
        startUpdatingMap(4000L)
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}