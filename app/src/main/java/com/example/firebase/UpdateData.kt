package com.example.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityUpdateDataBinding
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDataBinding

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.reference.child("MyTaskList")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()

        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnUpdate.setOnClickListener {
            val updateTaskName = binding.edtUpdateGetName.text.toString()
            val updateTaskDetail = binding.edtUpdateGetTask.text.toString()

            updateData(updateTaskName, updateTaskDetail)
        }
    }

    private fun updateData(updateTaskName: String, updateTaskDetail: String) {
        val taskId = intent.getStringExtra("taskId").toString()
        val mapData = mapOf(
            "taskDetails" to updateTaskDetail, "taskName" to updateTaskName, "userId" to taskId
        )
        reference.child(taskId).updateChildren(mapData).addOnCompleteListener {
            Toast.makeText(this, "Data Updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Data not updated.....", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun setData() {
        val txtName = intent.getStringExtra("name").toString()
        val txtDetail = intent.getStringExtra("details").toString()

        binding.edtUpdateGetName.setText(txtName)
        binding.edtUpdateGetTask.setText(txtDetail)
    }
}