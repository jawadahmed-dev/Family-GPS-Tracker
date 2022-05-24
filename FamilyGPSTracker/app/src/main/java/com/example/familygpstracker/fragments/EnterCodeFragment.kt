package com.example.familygpstracker.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.familygpstracker.R
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.apis.ChildService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.FragmentEnterCodeBinding
import com.example.familygpstracker.models.PairingCode
import com.example.familygpstracker.utility.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EnterCodeFragment : Fragment() {

    private lateinit var binding: FragmentEnterCodeBinding
    private lateinit var sessionManager :  SessionManager
    private val directionToConnectDeviceFragment = EnterCodeFragmentDirections.actionEnterCodeFragment2ToConnectDeviceFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMembers()
        changeStatusBarColor()
        registerListeners()
    }

    private fun initDataMembers() {
        sessionManager = SessionManager(requireActivity())
    }

    private fun registerListeners() {

        binding.linkDeviceBtn.setOnClickListener{view ->

            var parentId = sessionManager.getParentId()
            var pairingCode = binding.pairingCodeText.text.toString()
            if(pairingCode != null){
                var retrofit = RetrofitHelper.buildRetrofit()
                var childService = retrofit.create(ChildService::class.java)

                GlobalScope.launch {

                    var result = childService.linkParent(parentId.toString(), PairingCode(pairingCode))

                    if (result != null && result.code() == 200) {
                        startActivity(Intent(activity, ParentActivity::class.java))
                        activity?.finish()
                    }
                }
            }
            else {
                Toast.makeText(
                    requireActivity(), "Field Can't be Empty",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
        binding.enterCodeBackBtn.setOnClickListener{view ->
            findNavController().navigate(directionToConnectDeviceFragment)
        }
    }

        fun changeStatusBarColor() {
            if (Build.VERSION.SDK_INT >= 21) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
                }
            }
        }
}
