package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Events

class EventLayoutAdapter(
    private var eventdata:MutableList<Events>,
    private val onItemClickListener: (position: Int) -> Unit):RecyclerView.Adapter<EventLayoutAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.eventlayout,parent,false)
        return EventViewHolder(view)
    }
    fun updateData(newEventList: MutableList<Events>) {
        eventdata = newEventList
        notifyDataSetChanged()
    }
    fun removeItem(position: Int) {
        eventdata.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current=eventdata[position]
        holder.bind(current)
//        holder.textViewOptions.setOnClickListener { onItemClickListener(position) }
        holder.textViewOptions.setOnClickListener {
            // Call the onItemClickListener and pass the position or any other relevant data.
            onItemClickListener(position)
        }

        }



    override fun getItemCount(): Int {
      return eventdata.size
    }
    inner class EventViewHolder(item: View):RecyclerView.ViewHolder(item){
        private val eventNameTextView: TextView = item.findViewById(R.id.eventnametv)
        private val eventDateTextView: TextView = item.findViewById(R.id.eventdatetv)
        private val eventTimeTextView: TextView = item.findViewById(R.id.eventtimetv)
        val textViewOptions: TextView = item.findViewById(R.id.textViewOptions)
//        val itemedit: ImageView = item.findViewById(R.id.itemedit)
        fun bind(eventdata: Events){
            eventNameTextView.text=eventdata.name
            eventDateTextView.text=eventdata.Date
            eventTimeTextView.text=eventdata.time
        }

    }

}