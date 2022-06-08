
package com.example.familygpstracker.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.familygpstracker.R
import com.example.familygpstracker.apis.ChildService
import com.example.familygpstracker.apis.ParentService
import com.example.familygpstracker.apis.RetrofitHelper
import com.example.familygpstracker.databinding.FragmentChildSignUpBinding
import com.example.familygpstracker.databinding.FragmentParentSignUpBinding
import com.example.familygpstracker.fragments.ParentSignUpFragmentArgs
import com.example.familygpstracker.fragments.ParentSignUpFragmentDirections
import com.example.familygpstracker.models.RegisterChild

import com.example.familygpstracker.models.RegisterParent
import com.example.familygpstracker.utility.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChildSignUpFragment : Fragment() {

    private val directionToDecideUserFragment = ChildSignUpFragmentDirections.actionChildSignUpFragmentToDecideUserFragment()

    private val args : ParentSignUpFragmentArgs by navArgs()
    private var isUserNameValid = false
    private var isEmailValid = false
    private var isPasswordValid = false
    private var isPhoneNumberValid = false
    private var isFormValid = false
    private lateinit var binding: FragmentChildSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor()
        registerListeners()
        // Toast.makeText(requireActivity(),args.userType, Toast.LENGTH_LONG).show()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.statusBarColor= resources.getColor(R.color.white,null)
            }
        }
    }

    private fun registerListeners() {

        binding.username.setOnFocusChangeListener{view,focused ->
            if(!focused){
                binding.usernameContainer.helperText= validateUsername()
            }
        }
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

        binding.signupBackBtn.setOnClickListener{ view ->
            findNavController().navigate(directionToDecideUserFragment)
        }
        binding.signUpBtn.setOnClickListener { view ->
            binding.progressBar.visibility = View.VISIBLE
            reValidate()
            submitForm()

        }
    }

    private fun submitForm() {

        if (isFormValid) {


            var payload = makePayLoad()

            var retrofit = RetrofitHelper.buildRetrofit()

            var childService = retrofit.create(ChildService::class.java)
            if (NetworkUtils.haveNetworkConnection(requireActivity())) {
                GlobalScope.launch {

                    try{
                        var result = childService.registerChild(payload)

                        binding.progressBar.visibility = View.INVISIBLE

                        if (result != null && result.code() == 201) {

                            result.errorBody()

                            withContext(Dispatchers.Main) {
                                showSuccessMessage()
                            }
                        } else {
                            Log.d("Error", "" + result.errorBody())
                            withContext(Dispatchers.Main) {
                                showFailureMessage()
                            }
                        }

                    }catch(e:Exception){
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireActivity(), "Failed to Connect to Network!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        binding.progressBar.visibility = View.INVISIBLE
                    }

                }

            }
            else {
                Toast.makeText(
                    requireActivity(), "No Internet Available!",
                    Toast.LENGTH_LONG
                ).show()
                binding.progressBar.visibility = View.INVISIBLE
            }
        }




    }

    private fun showSuccessMessage() {

        var layout : View = requireActivity().layoutInflater.inflate(R.layout.success_message , null)
        var alertDialog = AlertDialog.Builder(requireActivity())
            .setView(layout)
            .create()
        alertDialog.show()
        layout.findViewById<Button>(R.id.succesMessageButton).setOnClickListener({
            alertDialog.dismiss()
        })
    }

    private fun showFailureMessage() {

        var layout : View = requireActivity().layoutInflater.inflate(R.layout.failure_message , null)
        var alertDialog = AlertDialog.Builder(requireActivity())
            .setView(layout)
            .create()
        alertDialog.show()
        layout.findViewById<Button>(R.id.failureMessageButton).setOnClickListener({
            alertDialog.dismiss()
        })
    }


    private fun makePayLoad() : RegisterChild{
        return RegisterChild(
            binding.username.text.toString(),
            binding.email.text.toString(),
            binding.password.text.toString()
           )
    }

    private fun reValidate(){
        isUserNameValid = validateUsername() == null
        isEmailValid = validateEmail() == null
        isPasswordValid = validatePassword() == null

        binding.usernameContainer.helperText=validateUsername()
        binding.passwordContainer.helperText=validatePassword()
        binding.emailContainer.helperText=validateEmail()

        if(!isEmailValid && !isPasswordValid && !isUserNameValid && !isPhoneNumberValid) isFormValid = false
        else isFormValid = true

    }

    /* private fun reValidate() {
         var username=binding.username.text.toString()
         var email=binding.email.text.toString()
         var phonenumber=binding.phonenumber.text.toString()
         var password = binding.password.text.toString()
         if((binding.username.isFocused && !username.isEmpty()) || (!binding.username.isFocused && username.isEmpty())){
             binding.usernameContainer.helperText=validateUsername()
         }
         if((binding.email.isFocused && !email.isEmpty()) || (!binding.email.isFocused && email.isEmpty())){
             binding.emailContainer.helperText=validateEmail()
         }
         if((binding.phonenumber.isFocused && !phonenumber.isEmpty()) || (!binding.phonenumber.isFocused && phonenumber.isEmpty())){
             binding.phonenumberContainer.helperText=validatePhonenumber()
         }
         if((binding.password.isFocused && !password.isEmpty()) || (!binding.password.isFocused && password.isEmpty())){
             binding.passwordContainer.helperText=validatePassword()
         }
     }
 */
    /* private fun submitForm() {
         val username = binding.usernameContainer.helperText == null
         val email = binding.emailContainer.helperText == null
         val phonenumber = binding.phonenumberContainer.helperText == null
         val password = binding.passwordContainer.helperText == null
         val direction=SignUpFragmentDirections.actionSignUpFragmentToDecideUserFragment()
         if(!username){
             binding.progressBar.visibility = View.INVISIBLE
             binding.username.requestFocus()
             return
         }
         if(!email){
             binding.progressBar.visibility = View.INVISIBLE
             binding.email.requestFocus()
             return
         }
         if(!phonenumber){
             binding.progressBar.visibility = View.INVISIBLE
             binding.phonenumber.requestFocus()
             return
         }
         if(!password){
             binding.progressBar.visibility = View.INVISIBLE
             binding.password.requestFocus()
             return
         }
         binding.progressBar.visibility = View.INVISIBLE
        // findNavController().navigate(direction)
     }
 */
    private fun validateUsername():String?{
        var username=binding.username.text.toString()
        if(!username.isEmpty()) {
            if (!username.matches(".*^[A-Za-z].*".toRegex())) {
                return "Must start with Alphabet"
            }
            if(!username.matches(".*^(?=.{8,20}\$).*".toRegex())){
                return "Must be 8 to 20 characters Long"
            }
            if (username.matches(".*[@#\$%^&+=].*".toRegex())) {
                return "Can't contain Special Character"
            }
            if(username.matches(".*[0-9].*".toRegex())){
                return "Can't not contain any Number"
            }
        }else {
            return "required"
        }
        return null
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
        if(!password.isEmpty()){
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