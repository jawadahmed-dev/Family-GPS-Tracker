package com.example.familygpstracker.fragments

import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.ChildService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.FragmentConnectDeviceBinding
import com.example.familygpstracker.databinding.FragmentGenerateCodeBinding
import com.example.familygpstracker.utility.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GenerateCodeFragment : Fragment() {
    private lateinit var binding:FragmentGenerateCodeBinding


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
        binding.refreshBtn.setOnClickListener{
            applyRotateAnimation()
            refreshPairingCode()
        }
    }

    private fun refreshPairingCode() {

        var childService = RetrofitHelper.getInstance().create(ChildService::class.java)
        GlobalScope.launch {
            var result = childService.getPairingCode("2C1852C3-07F6-4976-98AA-38DFF2C550CF")

            if(result!=null && result.code()==200){

                withContext(Dispatchers.Main){
                    binding.refreshBtn.clearAnimation()
                    binding.pairingCodeText.text = result.body()?.code
                }

            }
            else {
                withContext(Dispatchers.Main){
                    binding.refreshBtn.clearAnimation()
                    Toast.makeText(
                        requireActivity(), result.message(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun applyRotateAnimation() {

        var rotateAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anim)
        binding.refreshBtn.animation = rotateAnim
        binding.refreshBtn.startAnimation(rotateAnim)

    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


}