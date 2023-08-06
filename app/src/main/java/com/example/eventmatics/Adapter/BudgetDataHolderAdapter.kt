package com.example.eventmatics.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task

class BudgetDataHolderAdapter(private val context: Context, private var budgetList: List<Budget>
, private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BudgetDataHolderAdapter.ViewHolder>() {
    private var filteredList: MutableList<Budget> = mutableListOf()

    init {
        filteredList.addAll(budgetList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.budgetdataholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = budgetList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item)
        }
    }
    fun setadapter(newlist:MutableList<Budget>){
        budgetList=newlist
        notifyDataSetChanged()
    }
    interface OnItemClickListener {
        fun onItemClick(budget: Budget)
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    fun updateList(filteredList: MutableList<Budget>) {
        this.filteredList.clear()
        this.filteredList.addAll(filteredList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val Budget_title: TextView = itemView.findViewById(R.id.Budget_title)
        private val Budget_Category: TextView = itemView.findViewById(R.id.Budget_Category)
        private val total_amount: TextView = itemView.findViewById(R.id.text_total_amount)
        private val BudgetNote: TextView = itemView.findViewById(R.id.BudgetNote)
        fun bind(item: Budget) {
            Budget_title.text = item.name
            Budget_Category.text = item.category
            total_amount.text=item.estimatedAmount
            BudgetNote.text = item.note
        }
    }
}
