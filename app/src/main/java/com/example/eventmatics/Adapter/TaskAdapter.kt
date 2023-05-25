package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Subtask_info

class TaskAdapter(val tasklist:List<Subtask_info>):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.subtask_info,parent,false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
       val current=tasklist[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int {
      return tasklist.size
    }
    inner class TaskViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val taskname:TextView=itemView.findViewById(R.id.subtaskname)
        val taskradio:TextView=itemView.findViewById(R.id.subtask_radiobutton)
        fun bind(subtaskInfo: Subtask_info){
            taskname.text=subtaskInfo.name
        }
    }
}