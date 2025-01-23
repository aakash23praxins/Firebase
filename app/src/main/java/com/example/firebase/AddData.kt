package com.example.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityAddDataBinding
import com.google.firebase.database.FirebaseDatabase

class AddData : AppCompatActivity() {
    private lateinit var binding: ActivityAddDataBinding

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference = database.reference.child("MyTaskList")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val taskName = binding.edtGetName.text.toString()
            val taskDetail = binding.edtGetTask.text.toString()
            addTaskToDatabase(taskName, taskDetail)
        }

    }

    private fun addTaskToDatabase(taskName: String, taskDetail: String) {
        val userId = reference.push().key.toString()

        val task = Task(userId, taskName, taskDetail)
        reference.child(userId).setValue(task).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Data Saved to database...", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}