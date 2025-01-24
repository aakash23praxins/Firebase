package com.example.firebase.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.MainActivity
import com.example.firebase.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogIn.setOnClickListener {
            val userName = binding.edtLoginUserName.text.toString()
            val userPassword = binding.edtLoginPassword.text.toString()

            loginWithFirebase(userName, userPassword)
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        binding.txtForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
        binding.btnPhone.setOnClickListener {
            val intent = Intent(this, PhoneAuth::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginWithFirebase(userName: String, userPassword: String) {
        auth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "UserName not found or password incorrect!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currencyUser = auth.currentUser
        if (currencyUser != null) {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}