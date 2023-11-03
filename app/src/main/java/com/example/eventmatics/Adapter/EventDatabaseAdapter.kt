package com.example.eventmatics.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.PaymentActivityAdapter
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.DatabaseNameDataClass
import com.google.android.material.button.MaterialButton

class EventDatabaseAdapter(val context: Context, private var DatabaseList: List<DatabaseNameDataClass>
                           , val onDatabaseChangeClick: (newDatabaseName: String) -> Unit,
                           private val itemClickListener: OnItemClickListener) :RecyclerView.Adapter<EventDatabaseAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDatabaseAdapter.Viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.databaseholder,parent,false)
        return Viewholder(view) }
interface OnItemClickListener{ fun onItemClick(DatabaseList:DatabaseNameDataClass) }
    override fun onBindViewHolder(holder: EventDatabaseAdapter.Viewholder, position: Int) {
        val databaselist=DatabaseList[position]
        holder.bind(databaselist)
        holder.DatabaseDelete.setOnClickListener { itemClickListener.onItemClick(databaselist) } }
    fun updateData(newList: MutableList<DatabaseNameDataClass>) {
        DatabaseList = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int { return DatabaseList.size }
    inner class Viewholder(itemview: View):RecyclerView.ViewHolder(itemview){
        val DatabaseName=itemview.findViewById<TextView>(R.id.DatabaseName)
        val DatabaseTime=itemview.findViewById<TextView>(R.id.DatabaseTime)
        val DatabaseDate=itemview.findViewById<TextView>(R.id.DatabaseDate)
        val DatabaseChange:MaterialButton=itemview.findViewById(R.id.DatabaseChange)
        val DatabaseDelete:ImageView=itemview.findViewById(R.id.DatabaseDelete)
        init {
            DatabaseChange.setOnClickListener {
                val newDatabaseName = DatabaseList[adapterPosition].DatabaseName
                onDatabaseChangeClick(newDatabaseName) } }
        fun bind(DatabaseList: DatabaseNameDataClass){
            DatabaseName.text=DatabaseList.DatabaseName
            DatabaseTime.text= DatabaseList.Time
            DatabaseDate.text=DatabaseList.Date
        }
        fun getsharedpreference(context: Context, key:String):String?{
            val sharedvalue=context.getSharedPreferences("Database", Context.MODE_PRIVATE)
            return sharedvalue.getString(key,null)
        } } }