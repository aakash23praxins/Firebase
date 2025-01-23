package com.example.firebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.databinding.ActivityMainBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataFromDatabase()


//        adapter = TaskAdapter(list, this)

//        binding.recyclerView.layoutManager = LinearLayoutManager(this)

//        binding.recyclerView.adapter = adapter

        binding.fabAddBtn.setOnClickListener {
            val intent = Intent(this, AddData::class.java)
            startActivity(intent)
        }

    }

    private fun getDataFromDatabase() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (taskList in snapshot.children) {
                    val data = taskList.getValue(Task::class.java)
                    if (data != null) {
                        list.add(data)
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
}