package com.example.firebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityUpdateDataBinding

class UpdateData : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}