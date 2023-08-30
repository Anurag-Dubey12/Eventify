package com.example.eventmatics.SQLiteDatabase.Dataclass

data class BudgetWithPayment(
    val budget: Budget,
    val payment:MutableList<Paymentinfo>
)
