package com.example.eventmatics.Database_DataClass

data class BudgetDetails(
    val name: String,
    val category: String,
    val note: String,
    val estimatedAmount: Double,
    val balance: String,
    val remaining: String,
    val paid: String
)
