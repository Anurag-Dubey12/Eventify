package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Guest

class GuestApdater(private var GuestList:MutableList<Guest>,
                   private val onitemclick:OnItemClickListener):RecyclerView.Adapter<GuestApdater.GuestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GuestApdater.GuestViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.guestcompanionloadlayout,parent,false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestApdater.GuestViewHolder, position: Int) {
       val Guestlist=GuestList[position]
        holder.bind(Guestlist)
        holder.itemView.setOnClickListener {
            onitemclick.onItemClik(Guestlist)
        }
    }
    fun setdata(newlist:MutableList<Guest>){
        GuestList=newlist
        notifyDataSetChanged()
    }
interface OnItemClickListener{
    fun onItemClik(guestlist:Guest)
}
    override fun getItemCount(): Int {
        return GuestList.size
    }
    inner class GuestViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val GuestName:TextView=itemview.findViewById(R.id.guestname)
        fun bind(Guest:Guest){
            GuestName.text=Guest.name
        }
    }

}