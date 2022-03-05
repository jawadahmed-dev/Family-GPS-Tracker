package com.example.familygpstracker.adapter;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.familygpstracker.fragments.GeofenceFragment
import com.example.familygpstracker.fragments.LiveTrackFragment
import com.example.familygpstracker.fragments.LocationHistoryFragment

class SectionPagerAdapter(fragmentManager: FragmentManager,lifeCycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager,lifeCycle){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return  when(position){
            0->{
                LiveTrackFragment()

            }
            1->{
                LocationHistoryFragment()
            }
            2->{
                GeofenceFragment()
            }
           else -> {
               Fragment()
           }
        }
    }


}
