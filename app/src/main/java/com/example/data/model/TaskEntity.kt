package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String = "General",
    val dueDate: String = "",
    val priority: String = "Medium",
    val isCompleted: Boolean = false,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)
