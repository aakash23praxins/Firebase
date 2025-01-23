package com.example.firebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val list: ArrayList<Task>,
    private val context: Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTaskName: TextView = itemView.findViewById(R.id.txtName)
        val txtTaskDetail: TextView = itemView.findViewById(R.id.txtTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val dataItem = list[position]
        holder.txtTaskName.text = dataItem.taskName
        holder.txtTaskDetail.text = dataItem.taskDetails

        holder.itemView.setOnClickListener {
            val intent = Intent(context, UpdateData::class.java).apply {
                putExtra("name", dataItem.taskName)
                putExtra("details", dataItem.taskDetails)
                putExtra("taskId",dataItem.userId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}