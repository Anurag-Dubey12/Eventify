package com.example.eventmatics.data_class

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskDataHolder(
    val taskName: String,
    val taskInfo: String,
    val taskDate: String,
    val subtaskInfo: String
): Parcelable
