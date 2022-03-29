package com.example.familygpstracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.familygpstracker.R


class MenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onResume() {
        super.onResume()
        /*(activity as AppCompatActivity?)!!.getWindow().setStatusBarColor(ContextCompat.getColor(requireActivity(),
            R.color.light_blue
        ))
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()*/
    }

    override fun onStop() {
        super.onStop()
        /*(activity as AppCompatActivity?)!!.getWindow().setStatusBarColor(ContextCompat.getColor(requireActivity(),
            R.color.light_cyan
        ))
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()*/
    }
}