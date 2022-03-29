package com.example.familygpstracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.adapter.LocationHistoryListAdapter
import com.example.familygpstracker.adapter.NotificationListAdapter
import com.example.familygpstracker.databinding.FragmentLocationHistoryBinding
import com.example.familygpstracker.models.Location
import com.example.familygpstracker.models.Notification
import com.example.familygpstracker.viewmodels.MainViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationHistoryFragment : Fragment() {

    private lateinit var binding: FragmentLocationHistoryBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var locationHistoryListAdapter: LocationHistoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocationHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMembers()
    }
    private fun initDataMembers() {

        initViewModel()
        initAdapter()
        setUpRecyclerView()
        registerListeners()
    }

    private fun registerListeners() {
        viewModel.lastTenLocations.observe(requireActivity()  , {
            locationHistoryListAdapter.setLocations(it)
        }  )
    }


    private fun setUpRecyclerView() {

        binding.locationHistoryRecyclerview.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
        binding.locationHistoryRecyclerview.adapter = locationHistoryListAdapter
    }

    private fun initAdapter() {
        locationHistoryListAdapter = LocationHistoryListAdapter(ArrayList<Location>(),requireActivity())
    }

    private fun initViewModel() {

        viewModel = (requireActivity() as ParentActivity).mainViewModel

    }


}