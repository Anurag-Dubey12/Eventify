package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventmatics.R
import com.example.eventmatics.data_class.GuestCompanion

class GuestCompanionApdater(private val GuestList:List<GuestCompanion>):RecyclerView.Adapter<GuestCompanionApdater.GuestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GuestCompanionApdater.GuestViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.guestcompanionloadlayout,parent,false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestCompanionApdater.GuestViewHolder, position: Int) {
       val Guestlist=GuestList[position]

        holder.bind(Guestlist)
    }

    override fun getItemCount(): Int {
        return GuestList.size
    }
    inner class GuestViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val guestimage:ImageView=itemview.findViewById(R.id.guestimg)
//        val guestname:TextView=itemView.findViewById(R.id.guestname)
        fun bind(Guest:GuestCompanion){
//            guestname.text = Guest.name

            Glide.with(itemView)
                .load(Guest.guestImg)
                .into(guestimage)

        }
    }

}