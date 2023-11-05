package com.example.eventmatics.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.TaskEntity
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskDataHolderAdpater(private val context:Context, private var taskList: MutableList<TaskEntity>,
                            private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<TaskDataHolderAdpater.ViewHolder>() {
    private var filteredList: MutableList<TaskEntity> = mutableListOf()
    fun removeItem(position: Int) {
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }
    init { filteredList.addAll(taskList) }
    fun setData(newList: MutableList<TaskEntity>) {

        taskList = newList
        GlobalScope.launch(Dispatchers.Main) {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.taskdataholder, parent, false)
        return ViewHolder(view)
    }
    interface OnItemClickListener { fun onItemClick(task: TaskEntity) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = taskList[position]
        holder.bind(data,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(data) } }
    override fun getItemCount(): Int { return taskList.size }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.Taskname)
        private val taskInfoTextView: TextView = itemView.findViewById(R.id.Task_info_show)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.task_date)
        private val task_category: TextView = itemView.findViewById(R.id.task_category)
        private val task_note: TextView = itemView.findViewById(R.id.task_note)
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val Data_Item_Selected: CheckBox = itemView.findViewById(R.id.Item_selected)
        fun bind(data: TaskEntity, position: Int) {
            taskNameTextView.text = data.taskName
            taskInfoTextView.text = data.taskStatus
            taskDateTextView.text = data.taskDate
            task_category.text = data.category
            task_note.text = data.taskNote
            val dao = RoomDatabaseManager.getEventsDao(context)
            GlobalScope.launch(Dispatchers.IO){

            val iscompleted=dao.isTaskCompleted(data.id)
            if(iscompleted){
                taskInfoTextView.setTextColor(Color.parseColor("#00FF00"))
                cardView.setBackgroundColor(Color.parseColor("#F5F5F5"))
            }
            else{
                taskInfoTextView.setTextColor(Color.parseColor("#808080"))
                cardView.setBackgroundColor(Color.WHITE)
            } } }
    }
            }
