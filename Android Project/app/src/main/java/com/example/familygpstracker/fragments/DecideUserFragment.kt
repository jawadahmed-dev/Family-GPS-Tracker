package com.example.familygpstracker.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.familygpstracker.R
import com.example.familygpstracker.databinding.FragmentDecideUserBinding


class DecideUserFragment : Fragment() {
    private var userType:String? = null
    private lateinit var binding: FragmentDecideUserBinding
    private var directionToParentSignUpFragment = DecideUserFragmentDirections.actionDecideUserFragmentToParentSignUpFragment()
    private var directionToChildSignUpFragment = DecideUserFragmentDirections.actionDecideUserFragmentToChildSignUpFragment()
    private val directionToLoginFragment = DecideUserFragmentDirections.actionDecideUserFragmentToLoginFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDecideUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor()
        registerListeners()
    }


    private fun registerListeners() {
        binding.familyManagerOption.setOnClickListener({view ->
            if(binding.familyManagerOption.isChecked){
                binding.familyMemberOption.isChecked = false
                userType="Family Manager"
            }
        })
        binding.familyMemberOption.setOnClickListener({view->
            if(binding.familyMemberOption.isChecked){
                binding.familyManagerOption.isChecked = false
                userType="Family Member"
            }
        })
        binding.backBtn.setOnClickListener({
            findNavController().navigate(directionToLoginFragment)
        })
        binding.nextBtn.setOnClickListener({view ->
            if(userType!=null){
                when (userType){
                    "Family Manager" -> findNavController().navigate(directionToParentSignUpFragment)
                    "Family Member"  -> findNavController().navigate(directionToChildSignUpFragment)
                }
                //findNavController().navigate(DecideUserFragmentDirections.actionDecideUserFragmentToSignUpFragment(userType))
            }else{
                Toast.makeText(requireActivity(),"Must Select User Type",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }
}