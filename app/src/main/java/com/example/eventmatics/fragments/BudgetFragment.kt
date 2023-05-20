package com.example.eventmatics.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.eventmatics.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BudgetFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_budget, container, false)

        val Pending:AppCompatButton=view.findViewById(R.id.buttonPending)
        val Paid:AppCompatButton=view.findViewById(R.id.buttonPaid)

        Pending.setOnClickListener {
            Pending.setBackgroundResource(R.drawable.addpaymentbutton)
        }
        Paid.setOnClickListener {
            Pending.setBackgroundResource(R.drawable.addpaymentbutton)
        }

        return view
    }


}