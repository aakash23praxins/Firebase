package com.example.firebase.Authentication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            val userName = binding.edtSignUserName.text.toString()
            val userPassword = binding.edtSignPassword.text.toString()

            signUpUser(userName, userPassword)
        }
    }

    private fun signUpUser(userName: String, userPassword: String) {
        auth.createUserWithEmailAndPassword(userName, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign Up completed", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Sign Up canceled", Toast.LENGTH_SHORT).show()
                }
            }
    }
}