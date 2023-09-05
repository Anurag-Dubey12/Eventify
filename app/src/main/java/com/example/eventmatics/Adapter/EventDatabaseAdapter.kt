package com.example.eventmatics.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameDataClass
import com.example.eventmatics.fragments.DatabaseNameHolder
import com.example.eventmatics.getSharedPreference

class EventDatabaseAdapter(val context: Context,private val DatabaseList: List<DatabaseNameDataClass>
, private val onItemClick: (position: Int) -> Unit) :RecyclerView.Adapter<EventDatabaseAdapter.Viewholder>() {
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
        val DatabaseChange:ImageView=itemview.findViewById(R.id.DatabaseChange)
        val DatabaseDelete:ImageView=itemview.findViewById(R.id.DatabaseDelete)
        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
        fun bind(DatabaseList: DatabaseNameDataClass){
            DatabaseName.text=DatabaseList.DatabaseName
            DatabaseTime.text= DatabaseList.Time
            DatabaseDate.text=DatabaseList.Date

        }
        fun getsharedpreference(context: Context, key:String):String?{
            val sharedvalue=context.getSharedPreferences("Database", Context.MODE_PRIVATE)
            return sharedvalue.getString(key,null)
        }
    }


}