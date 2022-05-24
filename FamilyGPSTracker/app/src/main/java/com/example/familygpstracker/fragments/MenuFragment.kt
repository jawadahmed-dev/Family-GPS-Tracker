package com.example.familygpstracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.familygpstracker.R
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.databinding.FragmentMenuBinding
import com.example.familygpstracker.utility.SessionManager


class MenuFragment : Fragment() {

    private lateinit var binding : FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerListener()
        bindDataToView()
    }

    private fun bindDataToView() {
        binding.profileText.text = (requireActivity() as ParentActivity).mainViewModel.parentDetail.value?.name
    }

    private fun registerListener() {
        binding.profileOptionLogout.setOnClickListener({
            (requireActivity() as ParentActivity).sessionManager.logoutUser()
        })
    }

}