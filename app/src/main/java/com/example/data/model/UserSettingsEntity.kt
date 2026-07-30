package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val profileType: String = "FRESHER", // "STUDENT", "FRESHER", "JOB_HOLDER", "CUSTOM"
    val userName: String = "Sajid Ahmed",
    val initialCash: Double = 25000.0,
    val salaryDay: Int = 1, // Day of month (1-31)
    val currencySymbol: String = "৳", // "৳", "$", "€", "₹", "£"
    val isDarkMode: Boolean = false,
    val isDataLoaded: Boolean = false
)
