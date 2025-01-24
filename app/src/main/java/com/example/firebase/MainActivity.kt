package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.Authentication.MainActivity
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var list = ArrayList<Task>()
    private lateinit var adapter: TaskAdapter
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference = database.reference.child("MyTaskList")
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataFromDatabase()

        binding.fabDeleteBtn.setOnClickListener {
            deleteAllData()
        }

        auth = FirebaseAuth.getInstance()
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Log out user successfully!!", Toast.LENGTH_SHORT).show()
            binding.btnLogout.visibility = View.GONE
            startActivity(Intent(this, MainActivity::class.java).apply {
                finish()
            })
        }
//        adapter = TaskAdapter(list, this)

//        binding.recyclerView.layoutManager = LinearLayoutManager(this)

//        binding.recyclerView.adapter = adapter

        binding.fabAddBtn.setOnClickListener {
            val intent = Intent(this, AddData::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskId = adapter.getUserId(viewHolder.adapterPosition)
                reference.child(taskId).removeValue()
                Toast.makeText(applicationContext, "Item is deleted...", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(binding.recyclerView)

    }

    private fun deleteAllData() {
        AlertDialog.Builder(this).setTitle("Want to Delete All Data??")
            .setMessage("For single item delete , you have to swipe left and right for deletion...")
            .setIcon(R.drawable.ic_delete).setNegativeButton("No", { dialog, position ->
                dialog.cancel()
            })
            .setPositiveButton("Yes", { dialog, position ->
                reference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "All Data Delete Data Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        list.clear()
                        adapter.notifyDataSetChanged()

                    }
                }
            })
            .show()
    }

    private fun getDataFromDatabase() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                if (list.isEmpty()) {
                    binding.fabDeleteBtn.visibility = View.GONE
                }
                for (taskList in snapshot.children) {
                    val data = taskList.getValue(Task::class.java)
                    if (data != null) {
                        list.add(data)
                        binding.fabDeleteBtn.visibility = View.VISIBLE
                    } else {
                        binding.fabDeleteBtn.visibility = View.GONE
                    }
                    adapter = TaskAdapter(list, this@MainActivity)
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getDataFromDatabase()
    }
}