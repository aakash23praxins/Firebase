package com.example.firebase.Authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.MainActivity
import com.example.firebase.databinding.ActivityPhoneAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuth : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var CODE = ""
    private lateinit var binding: ActivityPhoneAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val code = p0.smsCode.toString()
                if (code != null) {
                    binding.edtVerificationCode.setText(code)
                    signWithCode(code)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("FIREBASE EXCEPTION", p0.message.toString())
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                CODE = p0
            }
        }
        binding.btnSendCode.setOnClickListener {
            val phoneNumber = binding.edtPhoneNumber.text.toString()

            val phoneAuthOption = PhoneAuthOptions.Builder(auth)
                .setPhoneNumber("+91$phoneNumber")
                .setTimeout(2L, TimeUnit.MINUTES)
                .setActivity(this@PhoneAuth)
                .setCallbacks(mCallBack)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOption)

        }

        binding.btnVerify.setOnClickListener {
            val code = binding.edtVerificationCode.text.toString()
            signWithCode(code)
        }


    }

    private fun signWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(CODE, code)
        signWithCredential(credential)
    }

    private fun signWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this@PhoneAuth, MainActivity::class.java).apply {
                    finish()
                })
            } else {
                Toast.makeText(applicationContext, "Wrong Pass Code", Toast.LENGTH_SHORT).show()
            }
        }
    }


}