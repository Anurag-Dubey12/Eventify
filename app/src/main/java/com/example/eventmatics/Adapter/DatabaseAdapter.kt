package com.example.eventmatics.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameHolder

class DatabaseAdapter(private val DatabaseList:List<DatabaseNameHolder>):RecyclerView.Adapter<DatabaseAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseAdapter.Viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.databaseholder,parent,false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: DatabaseAdapter.Viewholder, position: Int) {
        val databaselist=DatabaseList[position]
        return holder.bind(databaselist)
    }

    override fun getItemCount(): Int {
        return DatabaseList.size
    }
    inner class Viewholder(itemview: View):RecyclerView.ViewHolder(itemview){

        fun bind(DatabaseList:DatabaseNameHolder){

        }
    }
}