package com.example.familygpstracker.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.familygpstracker.fragments.GeofenceFragment
import com.example.familygpstracker.fragments.LiveTrackFragment

class ViewPagerAdapter(val fragmentManager: FragmentManager,val lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                LiveTrackFragment.newInstance();}
            1 -> {GeofenceFragment.newInstance();}
            else -> {
                LiveTrackFragment.newInstance();}
        }
    }
}