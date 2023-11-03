package com.example.eventmatics.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Guest

class GuestApdater(val context: Context, private var GuestList:MutableList<Guest>,
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
        holder.bind(Guestlist,position)
        holder.itemView.setOnClickListener {onitemclick.onItemClik(Guestlist) } }
    fun setdata(newlist:MutableList<Guest>){
        GuestList=newlist
        notifyDataSetChanged()
    }
interface OnItemClickListener{ fun onItemClik(guestlist: Guest) }
    override fun getItemCount(): Int { return GuestList.size }
    inner class GuestViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val GuestName:TextView=itemview.findViewById(R.id.guestname)
        val cardView:CardView=itemview.findViewById(R.id.cardView)
        fun bind(Guest: Guest, position:Int){
            GuestName.text=Guest.name
            val databasename = getSharedPreference(context, "databasename").toString()
            val db = LocalDatabase(context, databasename)
            val isInvitationsent=db.isInvitationsent(Guest.id)
            if(isInvitationsent){ cardView.setBackgroundColor(Color.parseColor("#F5F5F5")) } }
        fun getSharedPreference(context: Context, key: String): String? {
            val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
            return sharedPref.getString(key, null)
        } } }