package com.example.familygpstracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.familygpstracker.R
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.adapter.ChildListAdapter
import com.example.familygpstracker.adapter.ViewPagerAdapter
import com.example.familygpstracker.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home,container,false)

//        binding.viewPager.adapter=SectionPagerAdapter(requireActivity().supportFragmentManager,requireActivity().lifecycle)
//        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position ->
//            when(position){
//                0->{
//                    tab.text="Real-time Location"
//                }
//                1->{
//                    tab.text="Location History"
//                }
//                2->{
//                    tab.text="Geofences"
//                }
//            }
//        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // var map : SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
       // map.getMapAsync(this)

        // SetUp RecyclerView
        setUpRecyclerView()

        // SetUp ViewPager
        setUpViewPager(view)

    }

    private fun setUpViewPager(view: View) {
        var tabLayout=view.findViewById<TabLayout>(R.id.tabLayout)
        var viewPager=view.findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = ViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle)
        TabLayoutMediator(tabLayout,viewPager){tab,pos ->
            when(pos){
                0 -> tab.text="Live Tracking"
                1 -> tab.text="Geofence"
                else -> tab.text="Live Tracking"
            }
        }.attach()
    }

    /* override fun onMapReady(p0: GoogleMap) {
         var rawalpindiCBlock: LatLng = LatLng(33.632687, 73.076726)
         p0.addMarker(MarkerOptions().position(rawalpindiCBlock).title("Rawalpindi C-Block"))
         p0.moveCamera(CameraUpdateFactory.newLatLng(rawalpindiCBlock))
     }*/

    private fun setUpRecyclerView(){
        var list = ArrayList<String>(3);
        list.add("john doe");
        list.add("john doe");
        list.add("john doe");
        var adapter = ChildListAdapter(list,requireActivity());
        binding.familyMembersListView.layoutManager= LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.familyMembersListView.adapter = adapter;
    }

}