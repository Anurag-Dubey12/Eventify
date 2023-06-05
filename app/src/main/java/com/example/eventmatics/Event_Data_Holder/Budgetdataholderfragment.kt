package com.example.eventmatics.Event_Data_Holder

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.BudgetDataHolderAdapter
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.data_class.BudgetDataHolderData
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Budgetdataholderfragment : Fragment() {
    private lateinit var budgetdataAdapter: BudgetDataHolderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var budgetList: MutableList<BudgetDataHolderData>
    lateinit var Newfab:FloatingActionButton
    companion object {
        const val BUDGET_ACTIVITY_REQUEST_CODE = 1
        const val BUDGET_DATA_EXTRA = "budget_data"
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_budgetdataholderfragment2, container, false)
        Newfab=view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.recyclerView)
        budgetList = mutableListOf()
        budgetdataAdapter = BudgetDataHolderAdapter(budgetList)
        recyclerView.adapter = budgetdataAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Newfab.setOnClickListener {
            Intent(requireContext(), BudgetDetails::class.java).also {
                startActivityForResult(it,BUDGET_ACTIVITY_REQUEST_CODE)
//                startActivity(it)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == BUDGET_ACTIVITY_REQUEST_CODE) {
            val bundle = data?.extras
            val budgetData = bundle?.getParcelable<BudgetDataHolderData>("budgetData")
            if (budgetData != null) {
                budgetList.add(budgetData)
                budgetdataAdapter.notifyDataSetChanged()
            }
        }
    }
}