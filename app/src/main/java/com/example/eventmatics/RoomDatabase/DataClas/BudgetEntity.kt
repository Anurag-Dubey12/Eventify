package com.example.eventmatics.RoomDatabase.DataClas

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("Budget")
@Parcelize
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    val id: Long = 0,
    @ColumnInfo(name = "Budget_Name")
    val name: String,
    @ColumnInfo(name = "Budget_Category")
    val category: String,
    @ColumnInfo(name = "Budget_Note")
    val note: String,
    @ColumnInfo(name = "Budget_Estimated")
    val estimatedAmount: String,
    @ColumnInfo(name = "Budget_Balance")
    val balance: String,
    @ColumnInfo(name = "Budget_Pending")
    val remaining: String,
    @ColumnInfo(name = "Budget_Paid")
    var paid: String
) : Parcelable

