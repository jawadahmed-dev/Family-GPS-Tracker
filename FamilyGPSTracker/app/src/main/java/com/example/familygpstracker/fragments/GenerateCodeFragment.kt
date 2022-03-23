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
import java.text.SimpleDateFormat
import java.util.*


class GenerateCodeFragment : Fragment() {
    private lateinit var binding:FragmentGenerateCodeBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenerateCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMembers()
        changeStatusBarColor()
        checkCodeExpiry()
        binding.refreshBtn.setOnClickListener{
            applyRotateAnimation()
            refreshPairingCode()
        }
    }

    private fun initDataMembers() {
        sessionManager = SessionManager(requireActivity())
    }

    private fun checkCodeExpiry() {
        var expiryTimeString = sessionManager.getExpiryTime()
        var currentTimeString = SimpleDateFormat("HH:mm a").format(Date())

        if(expiryTimeString == null){
            expiryTimeString = getCodeExpiryTime(currentTimeString)
            sessionManager.setExpiryTime(expiryTimeString)
            binding.pairingCodeInfo.text ="*Your code is only valid till "+ expiryTimeString
        }
        else if(isExpiryTimeHasPassed(expiryTimeString,currentTimeString)){

            binding.pairingCodeInfo.text = "your code has expired."
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.pairingCodeInfo.setTextColor(requireActivity().getColor(R.color.cherry_red_opacity_55))
            }
        }
    }

    private fun isExpiryTimeHasPassed(expiryTimeString:String, currentTimeString:String): Boolean {
        var expiryTime : Date = SimpleDateFormat("HH:mm a").parse(expiryTimeString)
        var currentTime : Date = SimpleDateFormat("HH:mm a").parse(currentTimeString)
        if(expiryTime.before(currentTime)) return true
        else return false
    }

    private fun refreshPairingCode() {

        var childService = RetrofitHelper.getInstance().create(ChildService::class.java)
        GlobalScope.launch {
            var result = childService.getPairingCode("2C1852C3-07F6-4976-98AA-38DFF2C550CF")

            if(result!=null && result.code()==200){

                withContext(Dispatchers.Main){
                    binding.refreshBtn.clearAnimation()
                    binding.pairingCodeText.text = result.body()?.code
                    var expiryTimeString = getCodeExpiryTime(SimpleDateFormat("HH:mm a").format(Date()))
                    binding.pairingCodeInfo.text ="*Your code is only valid till " + expiryTimeString
                    sessionManager.setExpiryTime(expiryTimeString)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.pairingCodeInfo.setTextColor(requireActivity().getColor(R.color.light_blue_opacity_50))
                    }
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
    private fun getCodeExpiryTime(time: String): String {

        var timeHour = getCodeExpiryTimeHour(time.substring(0,2).toInt())
        var timePeriod = getCodeExpiryTimePeriod(time.substring(6,8),timeHour)
        var timeMinute = time.substring(3,5)
        return timeHour.toString() + ":" + timeMinute + " " + timePeriod
    }

    private fun getCodeExpiryTimePeriod(period : String , hour : Int) : String {

        if(hour == 12 || hour == 1 || hour == 2){
            when(period){
                "am" -> return "pm"
                "pm" -> return "am"
            }
        }
        return period
    }

    private fun getCodeExpiryTimeHour(hour : Int) : Int{

        if(hour >= 11) return (hour - 0)
        else return (hour + 2)
    }


}