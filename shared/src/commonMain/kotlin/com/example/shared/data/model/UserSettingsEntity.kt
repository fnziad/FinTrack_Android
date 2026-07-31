package com.example.shared.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val profileType: String = "CUSTOM", // "STUDENT", "FRESHER", "JOB_HOLDER", "CUSTOM"
    val userName: String = "",
    val initialCash: Double = 0.0,
    val salaryDay: Int = 1, // Day of month (1-31)
    val currencySymbol: String = "৳", // "৳", "$", "€", "₹", "£"
    val targetSavings: Double = 0.0,
    val targetBudget: Double = 0.0,
    val incomeFrequency: String = "Monthly", // "Daily", "Weekly", "Monthly", "Custom/Irregular"
    val colorTheme: String = "INDIGO", // "INDIGO", "EMERALD", "OCEAN", "TEAL", "ROSE"
    val isDarkMode: Boolean = false,
    val isDataLoaded: Boolean = false
)
