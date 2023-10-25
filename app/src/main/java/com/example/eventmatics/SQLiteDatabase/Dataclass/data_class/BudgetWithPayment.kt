package com.example.eventmatics.SQLiteDatabase.Dataclass.data_class

data class BudgetWithPayment(
    val budget: Budget,
    val payment:MutableList<Paymentinfo>
)
