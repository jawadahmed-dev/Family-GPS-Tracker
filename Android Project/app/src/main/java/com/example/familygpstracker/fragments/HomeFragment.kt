package com.example.familygpstracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.familygpstracker.R
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.activities.LinkDeviceActivity
import com.example.familygpstracker.adapter.ChildListAdapter
import com.example.familygpstracker.adapter.ViewPagerAdapter
import com.example.familygpstracker.databinding.FragmentHomeBinding
import com.example.familygpstracker.models.Child
import com.example.familygpstracker.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var childListAdapter: ChildListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMembers()
        setHasOptionsMenu(true);

        // SetUp RecyclerView
        setUpRecyclerView()

        // SetUp ViewPager
        setUpViewPager(view)

        // Register Observer
        registerObserver()
    }

    private fun registerObserver() {
        viewModel.parentDetail.observe(requireActivity()  , {
            childListAdapter.setChildrens(it.children)
        }  )
        /*viewModel.selectedChildInfo.observe(requireActivity(),{
            childListAdapter.setLastIndex(it.get(1).toInt())
        })*/
    }

    private fun initDataMembers() {
        initMainViewModel()
        initChildListAdapter()

    }

    private fun initChildListAdapter() {
        childListAdapter = ChildListAdapter(ArrayList<Child>(),requireActivity())
    }

    private fun initMainViewModel() {
        viewModel = (requireActivity() as ParentActivity).mainViewModel
    }

    private fun setUpViewPager(view: View) {
        var tabLayout=view.findViewById<TabLayout>(R.id.tabLayout)
        var viewPager=view.findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = ViewPagerAdapter(childFragmentManager,lifecycle)
        TabLayoutMediator(tabLayout,viewPager){tab,pos ->
            when(pos){
                0 -> tab.text="Live Tracking"
                1 -> tab.text="Geofence"
                else -> tab.text="Live Tracking"
            }
        }.attach()
    }


    private fun setUpRecyclerView(){
        var list = ArrayList<String>(3);
        list.add("john doe");
        list.add("john doe");
        list.add("john doe");
        var adapter = childListAdapter
        binding.familyMembersListView.layoutManager= LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.familyMembersListView.adapter = adapter;
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu,inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        var intent = Intent(requireActivity(),LinkDeviceActivity::class.java)
        intent.putExtra("key",0)
        startActivity(intent)
        requireActivity().finish()
        return true
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

}