package com.example.familygpstracker.fragments

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.apis.UserService
import com.example.familygpstracker.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.widget.Switch
import com.example.familygpstracker.activities.ChildLinkDeviceActivity
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.activities.ParentLinkDeviceActivity
import com.example.familygpstracker.models.User
import com.example.familygpstracker.utility.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LoginFragment : Fragment(R.layout.fragment_login) {

    private var auth=  FirebaseAuth.getInstance()
    private var isEmailValid = false
    private var isPasswordValid = false
    private var isFormValid = false
    private lateinit var binding : FragmentLoginBinding
    private val directionToDecideUserFragment=LoginFragmentDirections.actionLoginFragmentToDecideUserFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor()
        registerListeners()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }

    private fun registerListeners() {

        binding.email.setOnFocusChangeListener{view,focused ->
            if(!focused){
                binding.emailContainer.helperText = validateEmail()
            }
        }

        binding.password.setOnFocusChangeListener{view,focused ->
            if(!focused){
                binding.passwordContainer.helperText= validatePassword()
            }
        }

        binding.signUpText.setOnClickListener{view ->

            findNavController().navigate(directionToDecideUserFragment)
        }

        binding.loginBtn.setOnClickListener { view ->

            enableProgressBar()
            reValidate()
            if(isFormValid) verifyCredentials()
            else disableProgressBar()

        }
    }

    private fun  verifyCredentials(){

        var retrofit = RetrofitHelper.getInstance()
        var userService = retrofit.create(UserService::class.java)

        GlobalScope.launch {

            var result = userService.getUser(binding.email.text.toString())
            binding.progressBar.visibility = View.INVISIBLE
            if(result!=null && result.code() == 200){

                var user = result.body()

                if(user?.parent != null && user?.parent?.password == binding.password.text.toString()){
                    createLoginSession(user)
                    navigateToNext(user?.userType)
                }
                else if(user?.child != null && user?.child?.password == binding.password.text.toString()) {
                    createLoginSession(user)
                    navigateToNext(user?.userType)
                }

            }
            else {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        requireActivity(), "Email & Pass not correct!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

    }

    private fun enableProgressBar()  { binding.progressBar.visibility = View.VISIBLE }
    private fun disableProgressBar()  { binding.progressBar.visibility = View.INVISIBLE }

    private fun createLoginSession(user: User ){

        when(user.userType){
            "parent" -> SessionManager(requireActivity()).createParentLoginSession(user.parent?.parentId,user.userType,user.userId)
            "child" ->  SessionManager(requireActivity()).createChildLoginSession(user.child?.childId,user.userType,user.userId)
        }

    }

    private fun navigateToNext(userType:String){
        when(userType){
            "parent" -> {
                requireActivity().startActivity(Intent(requireActivity(),ParentLinkDeviceActivity::class.java))
                requireActivity().finish()
            }
            "child" -> {
                requireActivity().startActivity(Intent(requireActivity(), ChildLinkDeviceActivity::class.java))
                requireActivity().finish()
            }
        }

    }

    private fun reValidate(){

        isEmailValid = validateEmail() == null
        isPasswordValid = validatePassword() == null

        binding.emailContainer.helperText=validateEmail()
        binding.passwordContainer.helperText=validatePassword()

        if(!isEmailValid && !isPasswordValid) isFormValid = false
        else isFormValid = true

    }

    private fun validateEmail(): String? {

        var email=binding.email.text.toString()

        if(!email.isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return "invalid email address"
            }
        }
        else {
            return "required"
        }
        return null
    }
    private fun validatePassword(): String? {
        var password = binding.password.text.toString()
        if(!password.isEmpty()) {
            if (password.length < 8) {
                return "Minimum 8 Character Password"
            }
            if (!password.matches(".*[A-Z].*".toRegex())) {
                return "Must contain 1 Upper-case Character"
            }
            if (!password.matches(".*[a-z].*".toRegex())) {
                return "Must contain 1 Upper-case Character"
            }
            if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
                return "Must contain 1 Special Character"
            }
        }else {
            return "required"
        }
        return null;
    }
}
