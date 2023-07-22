package com.example.eventmatics.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Events_Data_Holder_Activity.TaskDataHolderActivity
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
class TaskAdapter(val context: Context, val tasklist: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.taskdataholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {
        val current = tasklist[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int {
        return minOf(tasklist.size, 5) // Limit to a maximum of 5 items
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.Taskname)
        private val taskInfoTextView: TextView = itemView.findViewById(R.id.Task_info_show)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.task_date)
        private val task_category: TextView = itemView.findViewById(R.id.task_category)
        private val task_note: TextView = itemView.findViewById(R.id.task_note)

        fun bind(data: Task) {
            taskNameTextView.text = data.taskName
            taskInfoTextView.text = data.taskStatus
            taskDateTextView.text = data.taskDate
            task_category.text = data.category
            task_note.text = data.taskNote
        }
    }

    fun addTask(task: Task) {
        if (tasklist.size >= 5) {
            // Remove the first item to make space for the new task
            tasklist.removeAt(0)
        }
        // Add the new task to the end of the list
        tasklist.add(task)
        notifyDataSetChanged()
    }
}
