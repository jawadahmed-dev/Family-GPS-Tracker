package com.example.familygpstracker.fragments

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
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.viewmodels.MainViewModel
import kotlinx.coroutines.*
import com.google.android.gms.maps.model.Marker





class LiveTrackFragment : Fragment() , OnMapReadyCallback {

    private lateinit var mHandler: Handler
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var map : SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private var isMapActive = true
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
        registerObserver()
        startUpdatingMap(4000L)

    }

    private fun goToLocation(latLang: LatLng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLang))
    }


    private fun registerObserver() {
        mainViewModel.location.observe(requireActivity(),{
            var latLang = LatLng(it.latitude.toDouble(),it.longitude.toDouble())
            moveMarker(latLang)
            goToLocation(latLang)

        })
    }

    private fun moveMarker(latLang: LatLng) {

        val marker: Marker? = googleMap.addMarker(MarkerOptions().position(latLang).title("Rawalpindi C-Block"))
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
        return CoroutineScope(Dispatchers.Default).launch {
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
        var rawalpindiCBlock: LatLng = LatLng(33.632687, 73.076726)
        p0.addMarker(MarkerOptions().position(rawalpindiCBlock).title("Rawalpindi C-Block"))
        p0.moveCamera(CameraUpdateFactory.newLatLng(rawalpindiCBlock))
    }
}