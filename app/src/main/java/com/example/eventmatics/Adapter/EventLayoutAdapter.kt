package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.EventEntity

open class EventLayoutAdapter(
    private var eventdata:MutableList<EventEntity>,
    private val onItemClickListener: (position: Int) -> Unit):RecyclerView.Adapter<EventLayoutAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.eventlayout,parent,false)
        return EventViewHolder(view)
    }
    open fun updateData(newEventList: MutableList<EventEntity>) {
        eventdata = newEventList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current=eventdata[position]
        holder.bind(current)
        holder.itemView.setOnClickListener { onItemClickListener(position) } }
    override fun getItemCount(): Int { return eventdata.size }
    inner class EventViewHolder(item: View):RecyclerView.ViewHolder(item){
        private val eventNameTextView: TextView = item.findViewById(R.id.eventnametv)
        private val eventDateTextView: TextView = item.findViewById(R.id.eventdatetv)
        private val eventTimeTextView: TextView = item.findViewById(R.id.eventtimetv)
        fun bind(eventdata: EventEntity){
            eventNameTextView.text=eventdata.name
            eventDateTextView.text=eventdata.date
            eventTimeTextView.text=eventdata.time
        } } }