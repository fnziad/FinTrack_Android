package com.example.shared.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import kotlinx.datetime.Clock

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "EXPENSE" or "INCOME"
    val amount: Double,
    val category: String,
    val subCategory: String,
    val description: String,
    val dateEpochMillis: Long = Clock.System.now().toEpochMilliseconds(),
    val dayName: String = "",
    val isRecurring: Boolean = false,
    val recurringFrequency: String = "One-time" // "Daily", "Weekly", "Monthly", "One-time"
)
