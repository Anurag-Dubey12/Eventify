package com.example.eventmatics.Event_Data_Holder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.BudgetDataHolderAdapter
import com.example.eventmatics.Adapter.PaymentActivity
import com.example.eventmatics.Adapter.TaskDataHolderData
import com.example.eventmatics.R
import com.example.eventmatics.data_class.BudgetDataHolderData
import com.example.eventmatics.data_class.Paymentinfo
import com.example.eventmatics.data_class.Subtask_info
import com.example.eventmatics.data_class.TaskDataHolder

class Taskdataholder : Fragment() {
    private lateinit var adapter: TaskDataHolderData
    private lateinit var paymentList: MutableList<TaskDataHolder>
    private lateinit var recyclerView: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_taskdataholder, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        paymentList = mutableListOf()// Replace with your logic to get payment data

        adapter = TaskDataHolderData(paymentList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val name = arguments?.getString("name").toString()
        val note = arguments?.getString("note").toString()
//        val data=Paymentinfo(name.toString(),12.0,"12")
//        paymentList.add(data)
        val data=TaskDataHolder(name,note,"12","Done")
        paymentList.add(data)
        return view
    }


}