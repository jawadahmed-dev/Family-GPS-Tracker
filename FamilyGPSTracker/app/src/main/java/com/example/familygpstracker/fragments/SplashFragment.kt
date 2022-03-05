package com.example.familygpstracker.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.familygpstracker.R


class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val direction=SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(direction)
        }, 2500)
        changeStatusBarColor()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.light_cyan,null)
            }
        }
    }
}