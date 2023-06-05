package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.data_class.TaskDataHolder

class TaskDataHolderData(private val dataList: List<TaskDataHolder>) :
    RecyclerView.Adapter<TaskDataHolderData.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.taskdataholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.Taskname)
        private val taskInfoTextView: TextView = itemView.findViewById(R.id.Task_info_show)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.task_date)
        private val subtaskInfoTextView: TextView = itemView.findViewById(R.id.subtask_info)

        fun bind(data: TaskDataHolder) {
            taskNameTextView.text = data.taskName
            taskInfoTextView.text = data.taskInfo
            taskDateTextView.text = data.taskDate
            subtaskInfoTextView.text = data.subtaskInfo
        }
    }
}
