package com.example.shared.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import kotlinx.datetime.Clock

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val targetDateEpochMillis: Long = Clock.System.now().toEpochMilliseconds() + (30L * 24 * 3600 * 1000),
    val category: String = "General",
    val note: String = ""
)
