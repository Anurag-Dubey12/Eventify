package com.example.eventmatics.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventmatics.data_class.BudgetDataHolderData


class BudgetViewModel : ViewModel() {
    val budgetList: MutableList<BudgetDataHolderData> = mutableListOf()
}
