package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events

class EventLayoutAdapter(val eventdata:MutableList<Events>,
                         private val onItemClickListener: (position: Int) -> Unit):RecyclerView.Adapter<EventLayoutAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.eventlayout,parent,false)
        return EventViewHolder(view)
    }
    fun removeItem(position: Int) {
        eventdata.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current=eventdata[position]
        holder.bind(current)
//        holder.itemView.setOnClickListener { onItemClickListener(position) }
//        holder.itemedit.setOnClickListener {
//            // Call the onItemClickListener and pass the position or any other relevant data.
//            onItemClickListener(position)
//        }
    }


    override fun getItemCount(): Int {
      return eventdata.size
    }
    inner class EventViewHolder(item: View):RecyclerView.ViewHolder(item){
        val eventNameTextView: TextView = item.findViewById(R.id.eventnametv)
        val eventDateTextView: TextView = item.findViewById(R.id.eventdatetv)
        val eventTimeTextView: TextView = item.findViewById(R.id.eventtimetv)
//        val itemedit: ImageView = item.findViewById(R.id.itemedit)
        fun bind(eventdata:Events){
            eventNameTextView.text=eventdata.name
            eventDateTextView.text=eventdata.Date
            eventTimeTextView.text=eventdata.time

        }

    }

}