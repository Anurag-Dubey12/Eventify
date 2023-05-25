package com.example.eventmatics.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Subtask_info
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TaskFragment : BottomSheetDialogFragment() {
    private lateinit var subtaskNameEt: EditText
    private lateinit var subtaskNoteEt: EditText
    private lateinit var subtaskPendingBut: AppCompatButton
    private lateinit var subtaskComBut: AppCompatButton
    private lateinit var subtaskSubmit: AppCompatButton

    interface UserDataListener{
        fun onUserDataEnter(userdata:Subtask_info)
    }
    private var userDataListener:TaskFragment.UserDataListener?=null

    fun setUserEnterDataListener(listner:TaskFragment.UserDataListener){
        userDataListener=listner
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subtaskNameEt = view.findViewById(R.id.SubtaskNameEt)
        subtaskNoteEt = view.findViewById(R.id.subtaskNoteET)
        subtaskPendingBut = view.findViewById(R.id.subtaskpendingbut)
        subtaskComBut = view.findViewById(R.id.subtaskcombut)
        subtaskSubmit = view.findViewById(R.id.subtasksubmit)

        subtaskSubmit.setOnClickListener {
            val name=subtaskNameEt.text.toString()

            val userdata=Subtask_info(name)

            userDataListener?.onUserDataEnter(userdata)
            dismiss()
        }

    }
}