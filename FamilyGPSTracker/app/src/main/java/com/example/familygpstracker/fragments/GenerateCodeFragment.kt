package com.example.familygpstracker.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.familygpstracker.databinding.FragmentConnectDeviceBinding
import com.example.familygpstracker.databinding.FragmentGenerateCodeBinding


class GenerateCodeFragment : Fragment() {
    /*private lateinit var binding:FragmentGenerateCodeBinding
    private val directionToConnectDeviceFragment = GenerateCodeFragmentDirections.actionGenerateCodeFragmentToConnectDeviceFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenerateCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor()
        binding.generateCodeBackBtn.setOnClickListener{
            findNavController().navigate(directionToConnectDeviceFragment)
        }
    }
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }
*/
}