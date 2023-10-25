package com.example.eventmatics.data_class

data class BudgetDatabase(
    val name: String = "",
    val category: String = "",
    val note: String = "",
    val estimatedAmount: Double = 0.0,
    val balance: Double = 0.0,
    val remaining: Double = 0.0,
    val paid: Double = 0.0
)
