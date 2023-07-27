package com.example.eventmatics.Adapter

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.example.eventmatics.SharedViewModel

class TaskDataHolderData(private val context:Context,private val dataList: List<Task>
,     private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<TaskDataHolderData.ViewHolder>() {
    private var filteredList: MutableList<Task> = mutableListOf()

    init {
        filteredList.addAll(dataList)
    }
    fun updateList(filteredList: MutableList<Task>) {
//        this.filteredList.clear()
        this.filteredList.addAll(filteredList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.taskdataholder, parent, false)
        return ViewHolder(view)

    }
    interface OnItemClickListener {
        fun onItemClick(task: Task)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
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
}
