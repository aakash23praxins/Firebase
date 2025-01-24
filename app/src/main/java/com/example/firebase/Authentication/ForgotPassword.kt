package com.example.firebase.Authentication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.btnForgot.setOnClickListener {
            val emailId = binding.edtEmail.text.toString()

            auth.sendPasswordResetEmail(emailId).addOnCompleteListener {
                task->
                if (task.isSuccessful){
                    Toast.makeText(this, "Email successfully sent", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Email not found in our database!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}