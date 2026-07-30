package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loans")
data class LoanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val personName: String,
    val amount: Double,
    val paidAmount: Double = 0.0,
    val loanType: String, // "SHORT_TERM" or "LONG_TERM"
    val direction: String, // "I_OWE" or "OWED_TO_ME"
    val dueDateEpochMillis: Long = System.currentTimeMillis() + (14L * 24 * 3600 * 1000),
    val isSettled: Boolean = false,
    val note: String = ""
)
