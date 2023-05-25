package com.example.eventmatics.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.example.eventmatics.Event_Details_Activity.VendorDetails
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Paymentinfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VendorFragment : BottomSheetDialogFragment() {
    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var vendorButtonPending: AppCompatButton
    private lateinit var vendorButtonPaid: AppCompatButton
    private lateinit var editTextDate: EditText
    private lateinit var buttonSubmit: AppCompatButton
    interface UserDataListener {
        fun onUserDataEntered(userData: Paymentinfo)
    }

    private var userDataListener: VendorFragment.UserDataListener? = null
    // Setter method for the UserDataListener
    fun setUserDataListener(listener: VendorFragment.UserDataListener) {
        userDataListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_vendor, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextName = view.findViewById(R.id.editTextName)
        editTextAmount = view.findViewById(R.id.editTextAmount)
        vendorButtonPending = view.findViewById(R.id.vendorbuttonPending)
        vendorButtonPaid = view.findViewById(R.id.vendorbuttonPaid)
        editTextDate = view.findViewById(R.id.editTextDate)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val name=editTextName.text.toString()
            val amount=editTextAmount.text.toString().toFloat()
            val paymentStatus = if (vendorButtonPending.isSelected) "Pending" else "Paid"
            val date=editTextDate.text.toString()

            val userdata=Paymentinfo(name,amount,paymentStatus,date)

            userDataListener?.onUserDataEntered(userdata)
            dismiss()
//When the user clicks the buttonSubmit, the onClick listener is triggered.
//The listener retrieves the values entered in the EditText fields and creates a Paymentinfo object with the provided data.
//The userDataListener is then called with the userData as an argument to pass the entered payment information.
//Finally, the VendorFragment is dismissed, closing the bottom sheet dialog.
        }
    }


}