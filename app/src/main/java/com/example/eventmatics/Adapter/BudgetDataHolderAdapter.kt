package com.example.eventmatics.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.BudgetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BudgetDataHolderAdapter(private val context: Context, private var budgetList: List<BudgetEntity>,
                              private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<BudgetDataHolderAdapter.ViewHolder>() {
    private var filteredList: MutableList<BudgetEntity> = mutableListOf()
    init { filteredList.addAll(budgetList) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.budgetdataholder, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = budgetList[position]
        holder.bind(item,position)
        holder.itemView.setOnClickListener { itemClickListener.onItemClick(item) } }
    fun setadapter(newlist:MutableList<BudgetEntity>){
        budgetList=newlist
        GlobalScope.launch(Dispatchers.IO){
        notifyDataSetChanged()
        }
    }
    interface OnItemClickListener { fun onItemClick(budget: BudgetEntity) }
    override fun getItemCount(): Int { return budgetList.size }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val Budget_title: TextView = itemView.findViewById(R.id.Budget_title)
        private val Budget_Category: TextView = itemView.findViewById(R.id.Budget_Category)
        private val total_amount: TextView = itemView.findViewById(R.id.text_total_amount)
        private val BudgetNote: TextView = itemView.findViewById(R.id.BudgetNote)
        private var BudgetPaid: TextView = itemView.findViewById(R.id.BudgetPaid)
        private val BudgetCardView: CardView = itemView.findViewById(R.id.BudgetCardView)
        fun bind(item: BudgetEntity, position: Int) {
            Budget_title.text = item.name
            Budget_Category.text = item.category
            total_amount.text = item.estimatedAmount
            BudgetNote.text = item.note
            BudgetPaid.text=item.paid
        }

    }}