package com.example.familygpstracker.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.familygpstracker.databinding.FragmentEnterCodeBinding
import com.example.familygpstracker.databinding.FragmentSignUpBinding


class EnterCodeFragment : Fragment() {

   /* private lateinit var binding: FragmentEnterCodeBinding
    private val directionToDecideUserFragment = EnterCodeFragmentDirections.actionEnterCodeFragmentToDecideUserFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor()
        registerListeners()
    }

    private fun registerListeners() {
        binding.linkDeviceBtn.setOnClickListener{view ->
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        binding.enterCodeBackBtn.setOnClickListener{view ->
            findNavController().navigate(directionToDecideUserFragment)
        }
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }*/
}