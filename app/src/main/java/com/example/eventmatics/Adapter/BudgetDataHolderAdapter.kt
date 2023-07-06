package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget

class BudgetDataHolderAdapter(private val budgetList: List<Budget>) : RecyclerView.Adapter<BudgetDataHolderAdapter.ViewHolder>() {
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
        private val eventNameTextView: TextView = itemView.findViewById(R.id.text_event_name)
        private val total_amount: TextView = itemView.findViewById(R.id.text_total_amount)
        fun bind(item: Budget) {
            eventNameTextView.text = item.name
            total_amount.text=item.estimatedAmount
        }
    }
}
