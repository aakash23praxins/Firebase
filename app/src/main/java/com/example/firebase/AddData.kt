package com.example.firebase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.firebase.databinding.ActivityAddDataBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddData : AppCompatActivity() {
    private lateinit var binding: ActivityAddDataBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference = database.reference.child("MyTaskList")

    private var firebaseStorage = FirebaseStorage.getInstance()
    private var imgReference = firebaseStorage.reference
    var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerActivityLauncher()

        binding.btnAdd.setOnClickListener {
//            uploadPhoto()
            addTaskToDatabase()
        }
        binding.imgGetImage.setOnClickListener {
            getImage()
        }
    }

    private fun uploadPhoto() {
        val imgUid = UUID.randomUUID().toString()

        val newImgRef = imgReference.child("images").child(imgUid)

        imageUri.let { uri ->
            if (uri != null) {
                newImgRef.putFile(uri).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        val downloadRef = imgReference.child("images").child(imgUid)

                        downloadRef.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            addTaskToDatabase(imageUrl)
                        }
                    }
                }.addOnFailureListener { task ->
                    println("=============================== ${task.message}")
                    Toast.makeText(this, "Firebase error ${task.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun registerActivityLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), { result ->
                val resultCode = result.resultCode
                val data = result.data
                if (resultCode == RESULT_OK && data != null) {
                    imageUri = data.data

                    Log.d("IMAGE", "data -- $data")
                    imageUri.let { Glide.with(this).load(it).into(binding.imgGetImage) }
                }
            })
    }

    private fun getImage() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                this, permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 200)
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    private fun addTaskToDatabase(imgUri: String? = null) {
        val taskName = binding.edtGetName.text.toString()
        val taskDetail = binding.edtGetTask.text.toString()
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