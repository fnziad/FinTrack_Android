package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "EXPENSE" or "INCOME"
    val amount: Double,
    val category: String,
    val subCategory: String,
    val description: String,
    val dateEpochMillis: Long = System.currentTimeMillis(),
    val dayName: String = ""
)
