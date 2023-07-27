package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameHolder
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events

class EventDatabaseAdapter(private val DatabaseList: MutableList<DatabaseNameHolder>):RecyclerView.Adapter<EventDatabaseAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDatabaseAdapter.Viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.databaseholder,parent,false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: EventDatabaseAdapter.Viewholder, position: Int) {
        val databaselist=DatabaseList[position]
        holder.bind(databaselist)
    }

    override fun getItemCount(): Int {
        return DatabaseList.size
    }
    inner class Viewholder(itemview: View):RecyclerView.ViewHolder(itemview){

        fun bind(DatabaseList: DatabaseNameHolder){

        }
    }
}