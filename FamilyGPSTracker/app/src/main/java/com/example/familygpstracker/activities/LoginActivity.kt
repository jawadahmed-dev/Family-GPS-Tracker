package com.example.familygpstracker.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import com.example.familygpstracker.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerListeners()

    }
    private fun registerListeners() {

        binding.name.setOnFocusChangeListener{view,focused ->
            println("hello")
//            if(focused){
//                binding.emailContainer.helperText = validateEmail()
//            }
//            else{
//                binding.emailContainer.helperText = validateEmail()
//            }
        }
        /*binding.email.setOnFocusChangeListener{view,focused ->
            println("hello")
            if(focused){
                binding.emailContainer.helperText = validateEmail()
            }
        }
        binding.password.setOnFocusChangeListener{view,focused ->
            if(focused){
                binding.passwordContainer.helperText= validatePassword()
            }
        }*/
    }

    private fun validateEmail(): String? {
        var email=binding.email.text.toString()
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "invalid email address"
        }
        return null
    }

}