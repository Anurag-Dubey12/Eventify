package com.example.eventmatics.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.example.eventmatics.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VendorFragment : BottomSheetDialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_vendor, container, false)

        val vendorpendingbut=view.findViewById<AppCompatButton>(R.id.vendorbuttonPending)
        val vendorpaidbut=view.findViewById<AppCompatButton>(R.id.vendorbuttonPaid)

        return view
    }


}