package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.data_class.BudgetDataHolderData

class BudgetDataHolderAdapter(private val budgetList: List<BudgetDataHolderData>) : RecyclerView.Adapter<BudgetDataHolderAdapter.ViewHolder>() {

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventNameTextView: TextView = itemView.findViewById(R.id.text_event_name)
        private val pendingAmountTextView: TextView = itemView.findViewById(R.id.text_pending_amount)
        private val transactionInfoTextView: TextView = itemView.findViewById(R.id.text_transaction_info)
        private val totalAmountTextView: TextView = itemView.findViewById(R.id.text_total_amount)
        private val paidAmountTextView: TextView = itemView.findViewById(R.id.text_paid_amount)

        fun bind(item: BudgetDataHolderData) {
            eventNameTextView.text = item.eventName
            pendingAmountTextView.text = "Pending Amount: ${item.pending}"
            totalAmountTextView.text = "Total Amount: ${item.amount}"
            paidAmountTextView.text = "Paid Amount: ${item.paidAmount}"
        }
    }
}
