package com.example.eventmatics.Adapter

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task

class TaskDataHolderData(private val context:Context,private val dataList: List<Task>) :
    RecyclerView.Adapter<TaskDataHolderData.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.taskdataholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskDetails::class.java).apply {
                putExtra("taskId", data.id) }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.Taskname)
        private val taskInfoTextView: TextView = itemView.findViewById(R.id.Task_info_show)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.task_date)

        fun bind(data: Task) {
            taskNameTextView.text = data.taskName
            taskInfoTextView.text = data.taskStatus
            taskDateTextView.text = data.taskDate
        }
    }
}
