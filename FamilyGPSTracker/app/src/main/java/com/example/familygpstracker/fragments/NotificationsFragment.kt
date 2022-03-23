package com.example.familygpstracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familygpstracker.R
import com.example.familygpstracker.adapter.NotificationListAdapter
import com.example.familygpstracker.apis.NotificationService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.FragmentNotificationsBinding
import com.example.familygpstracker.models.Notification
import com.example.familygpstracker.repositories.NotificationRepository
import com.example.familygpstracker.viewmodels.MainViewModel
import com.example.familygpstracker.viewmodels.MainViewModelFactory
import com.example.familygpstracker.viewmodels.NotificationViewModel
import com.example.familygpstracker.viewmodels.NotificationViewModelFactory


class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notificationListAdapter: NotificationListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMembers()
        setUpRecyclerView()
        registerListeners()
    }

    private fun registerListeners() {
        notificationViewModel.notificationList.observe(requireActivity()  , {
            notificationListAdapter.setNotifications(it)
        }  )
    }

    private fun setUpRecyclerView() {

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.notificationRecyclerView.adapter = notificationListAdapter
    }

    private fun initDataMembers() {

        initViewModel()
        initAdapter()

    }

    private fun initAdapter() {
        notificationListAdapter = NotificationListAdapter(ArrayList<Notification>(),requireActivity())
    }

    private fun initViewModel() {
        var notificationService = RetrofitHelper.getInstance().create(NotificationService::class.java)
        var notificationRepository = NotificationRepository(notificationService)
        notificationViewModel = ViewModelProvider(requireActivity(),
            NotificationViewModelFactory(notificationRepository)
        ).get(NotificationViewModel::class.java)
    }

}