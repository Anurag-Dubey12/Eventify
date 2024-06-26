package com.example.eventmatics.data_class

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class BudgetDataHolderData(
    val eventName: String,
    val pending: String,
    val amount: Float,
    val paidAmount: String,
    val traninfo:String
):Parcelable