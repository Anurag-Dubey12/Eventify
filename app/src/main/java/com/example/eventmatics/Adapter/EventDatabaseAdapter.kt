package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameDataClass

class EventDatabaseAdapter(private val DatabaseList: List<DatabaseNameDataClass>):RecyclerView.Adapter<EventDatabaseAdapter.Viewholder>() {
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
        val DatabaseName=itemview.findViewById<TextView>(R.id.DatabaseName)
        val DatabaseTime=itemview.findViewById<TextView>(R.id.DatabaseTime)
        val DatabaseDate=itemview.findViewById<TextView>(R.id.DatabaseDate)
        fun bind(DatabaseList: DatabaseNameDataClass){
            DatabaseName.text=DatabaseList.DatabaseName
            DatabaseTime.text= DatabaseList.Time
            DatabaseDate.text=DatabaseList.Date
        }
    }
}