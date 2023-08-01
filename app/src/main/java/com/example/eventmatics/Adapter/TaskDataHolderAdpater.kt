package com.example.eventmatics.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task

class TaskDataHolderAdpater(private val context:Context, private val dataList: MutableList<Task>
                            , private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<TaskDataHolderAdpater.ViewHolder>() {
    private var filteredList: MutableList<Task> = mutableListOf()
    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

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
        holder.itemView.setOnLongClickListener {
            removeItem(position)
            true // Return 'true' to indicate that the long click event is consumed.
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
