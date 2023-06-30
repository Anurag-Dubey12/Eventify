package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.example.eventmatics.data_class.Eventlayourdata

class EventLayoutAdapter(val eventdata:List<Events>):RecyclerView.Adapter<EventLayoutAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
val view=LayoutInflater.from(parent.context).inflate(R.layout.eventlayout,parent,false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current=eventdata[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int {
      return eventdata.size
    }
    inner class EventViewHolder(item: View):RecyclerView.ViewHolder(item){
        val eventNameTextView: TextView = item.findViewById(R.id.eventnametv)
        val eventDateTextView: TextView = item.findViewById(R.id.eventdatetv)
        val eventTimeTextView: TextView = item.findViewById(R.id.eventtimetv)
        fun bind(eventdata:Events){
            eventNameTextView.text=eventdata.name
            eventDateTextView.text=eventdata.Date
            eventTimeTextView.text=eventdata.time
        }

    }

}