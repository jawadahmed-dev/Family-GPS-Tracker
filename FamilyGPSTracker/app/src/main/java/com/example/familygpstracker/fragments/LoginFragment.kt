package com.example.familygpstracker.fragments

import android.app.Activity
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
import com.example.familygpstracker.databinding.FragmentLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import com.example.familygpstracker.activities.AuthenticationActivity
import com.example.familygpstracker.activities.ChildActivity
import com.example.familygpstracker.activities.LinkDeviceActivity

import com.example.familygpstracker.models.User
import com.example.familygpstracker.utility.NetworkUtils
import com.example.familygpstracker.utility.SessionManager
import com.example.familygpstracker.viewmodels.AuthenticationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class LoginFragment : Fragment(R.layout.fragment_login) {


    private lateinit var authViewModel : AuthenticationViewModel
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
        initDataMembers()
        registerEventListeners()
        registerObservers()
    }

    private fun registerObservers() {

        authViewModel.userLoginResult.observe(requireActivity(),{

            binding.progressBar.visibility = View.INVISIBLE

            if (isAdded) {
                if (it.code() == 200) {

                    var user = it.body()

                    if (user?.parent?.password == binding.password.text.toString() || user?.child?.password == binding.password.text.toString()) {
                        createLoginSession(user)
                        navigateToNext(user?.userType)
                    }
                    else {
                        Toast.makeText(
                            requireActivity(), "Password is not correct!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else {
                    Toast.makeText(
                        requireActivity(), "Email is not correct!",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        })
    }

    private fun initDataMembers() {
       authViewModel = (requireActivity() as AuthenticationActivity).authViewModel

    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }

    private fun registerEventListeners() {

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
            if(isFormValid) verifyCredentialsFromServer()
            else disableProgressBar()

        }
    }

    private fun  verifyCredentialsFromServer(){

       /* var retrofit = RetrofitHelper.buildRetrofit()
        var userService = retrofit.create(UserService::class.java)*/
        if(!NetworkUtils.haveNetworkConnection(requireActivity())) {

            Toast.makeText(
                requireActivity(), "No Internet Available!",
                Toast.LENGTH_LONG
            ).show()
            disableProgressBar()
            return
        }

        GlobalScope.launch {

                try {
                    authViewModel.getUserLoginResult(binding.email.text.toString())
                } catch (e: Exception) {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireActivity(), "Failed to Connect to Network!",
                            Toast.LENGTH_LONG
                        ).show()
                        disableProgressBar()
                    }
                }
        }

/*

            if(NetworkUtils.haveNetworkConnection(requireActivity())){
                GlobalScope.launch {

                    try {

                    var result = userService.getUser(binding.email.text.toString())
                    binding.progressBar.visibility = View.INVISIBLE
                    if (result != null && result.code() == 200) {

                        var user = result.body()

                        if (user?.parent != null && user?.parent?.password == binding.password.text.toString()) {
                            createLoginSession(user)
                            navigateToNext(user?.userType)
                        } else if (user?.child != null && user?.child?.password == binding.password.text.toString()) {
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
                    catch (e : SocketTimeoutException){
                        withContext(Dispatchers.Main){
                            Toast.makeText(
                                requireActivity(), "Failed to Connect to Network!",
                                Toast.LENGTH_LONG
                            ).show()
                            disableProgressBar()
                        }

                    }

                }

            }
            else {
                Toast.makeText(
                    requireActivity(), "No Internet Available!",
                    Toast.LENGTH_LONG
                ).show()
                disableProgressBar()
            }

*/


    }

    private fun enableProgressBar()  { binding.progressBar.visibility = View.VISIBLE }
    private fun disableProgressBar()  { binding.progressBar.visibility = View.INVISIBLE }

    private fun createLoginSession(user: User ){

        when(user.userType){
            "parent" -> SessionManager(requireActivity()).createParentLoginSession(user.parent?.parentId,user.userType,user.userId)
            "child" ->  SessionManager(requireActivity()).createChildLoginSession(user.child?.childId,user.userType,user.userId,user.child?.parentId)
        }

    }

    private fun navigateToNext(userType:String){
        when(userType){
            "parent" -> {
                requireActivity().startActivity(Intent(requireActivity(), LinkDeviceActivity::class.java))
                requireActivity().finish()
            }
            "child" -> {
                requireActivity().startActivity(Intent(requireActivity(), ChildActivity::class.java))
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

        var email=binding.email.text.toString().trim()

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
                return "Must contain 1 Lower-case Character"
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
