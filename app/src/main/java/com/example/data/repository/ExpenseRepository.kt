package com.example.data.repository

import com.example.data.dao.LoanDao
import com.example.data.dao.SavingsGoalDao
import com.example.data.dao.TransactionDao
import com.example.data.dao.UserSettingsDao
import com.example.data.model.LoanEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserSettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class ExpenseRepository(
    private val transactionDao: TransactionDao,
    private val savingsGoalDao: SavingsGoalDao,
    private val loanDao: LoanDao,
    private val userSettingsDao: UserSettingsDao
) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val allSavingsGoals: Flow<List<SavingsGoalEntity>> = savingsGoalDao.getAllSavingsGoals()
    val allLoans: Flow<List<LoanEntity>> = loanDao.getAllLoans()
    val userSettings: Flow<UserSettingsEntity?> = userSettingsDao.getUserSettings()

    suspend fun insertTransaction(transaction: TransactionEntity) = transactionDao.insertTransaction(transaction)
    suspend fun updateTransaction(transaction: TransactionEntity) = transactionDao.updateTransaction(transaction)
    suspend fun deleteTransaction(id: Int) = transactionDao.deleteTransactionById(id)

    suspend fun insertSavingsGoal(goal: SavingsGoalEntity) = savingsGoalDao.insertSavingsGoal(goal)
    suspend fun updateSavingsGoal(goal: SavingsGoalEntity) = savingsGoalDao.updateSavingsGoal(goal)
    suspend fun deleteSavingsGoal(id: Int) = savingsGoalDao.deleteSavingsGoalById(id)

    suspend fun insertLoan(loan: LoanEntity) = loanDao.insertLoan(loan)
    suspend fun updateLoan(loan: LoanEntity) = loanDao.updateLoan(loan)
    suspend fun deleteLoan(id: Int) = loanDao.deleteLoanById(id)

    suspend fun saveUserSettings(settings: UserSettingsEntity) = userSettingsDao.saveUserSettings(settings)

    suspend fun clearAllData() {
        transactionDao.deleteAllTransactions()
        savingsGoalDao.deleteAll()
        loanDao.deleteAll()
    }

    suspend fun seedSampleDataIfEmpty(preset: String = "FRESHER") {
        restoreSampleData(preset = preset, keepUserSettings = false)
    }

    suspend fun restoreSampleData(preset: String = "FRESHER", keepUserSettings: Boolean = true) {
        val now = System.currentTimeMillis()
        val dayMs = 24L * 3600 * 1000

        if (keepUserSettings) {
            val current = userSettingsDao.getUserSettings().firstOrNull() ?: UserSettingsEntity()
            val updated = current.copy(
                profileType = preset,
                isDataLoaded = true
            )
            userSettingsDao.saveUserSettings(updated)
        } else {
            val settings = UserSettingsEntity(
                id = 1,
                profileType = preset,
                userName = if (preset == "STUDENT") "Nafis Mahmud" else "Sajid Ahmed",
                initialCash = if (preset == "STUDENT") 12000.0 else 30000.0,
                salaryDay = if (preset == "STUDENT") 5 else 1,
                currencySymbol = "৳",
                isDarkMode = false,
                isDataLoaded = true
            )
            userSettingsDao.saveUserSettings(settings)
        }

        clearAllData()

        // Seed BD specific sample transactions
        val sampleTransactions = listOf(
            TransactionEntity(
                type = "INCOME",
                amount = if (preset == "STUDENT") 12000.0 else 30000.0,
                category = "Salary & Income",
                subCategory = if (preset == "STUDENT") "Pocket Money / Allowance" else "Monthly Salary",
                description = if (preset == "STUDENT") "Monthly allowance from father" else "Tech Solutions Ltd. Salary",
                dateEpochMillis = now - (15 * dayMs),
                dayName = "1st"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = if (preset == "STUDENT") 4500.0 else 8000.0,
                category = "Food & Mess",
                subCategory = "Mess Meal & Rent",
                description = "Mess meal bill & seat rent for month",
                dateEpochMillis = now - (14 * dayMs),
                dayName = "2nd"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 1200.0,
                category = "Transport",
                subCategory = "Metro Rail Pass",
                description = "MRT Pass top-up (Uttara to Farmgate)",
                dateEpochMillis = now - (12 * dayMs),
                dayName = "4th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 350.0,
                category = "Transport",
                subCategory = "Rickshaw Fare",
                description = "Rickshaw from Mess to Bus Stand",
                dateEpochMillis = now - (10 * dayMs),
                dayName = "6th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 450.0,
                category = "Food & Mess",
                subCategory = "Tea Stall & Snacks",
                description = "Tong tea, shingara & evening snacks with friends",
                dateEpochMillis = now - (8 * dayMs),
                dayName = "8th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 850.0,
                category = "Utilities & Mobile",
                subCategory = "Mobile Recharge & Data",
                description = "Robi 30-day data pack + talktime",
                dateEpochMillis = now - (6 * dayMs),
                dayName = "10th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 150.0,
                category = "Utilities & Mobile",
                subCategory = "Bkash Cashout Fee",
                description = "Bkash cashout fee at agent point",
                dateEpochMillis = now - (4 * dayMs),
                dayName = "12th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 2500.0,
                category = "Education & Tuition",
                subCategory = "Tuition / Course Fee",
                description = if (preset == "STUDENT") "Semester mid-term exam fee" else "Skill development online course",
                dateEpochMillis = now - (3 * dayMs),
                dayName = "13th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 1800.0,
                category = "Shopping & Personal",
                subCategory = "Clothes & Shoes",
                description = "Formal shirt from Elephant Road",
                dateEpochMillis = now - (1 * dayMs),
                dayName = "Yesterday"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 180.0,
                category = "Food & Mess",
                subCategory = "Tea Stall & Snacks",
                description = "Raw tea + Paratha lunch",
                dateEpochMillis = now,
                dayName = "Today"
            )
        )
        transactionDao.insertAll(sampleTransactions)

        // Seed Savings goals
        val sampleGoals = listOf(
            SavingsGoalEntity(
                title = "Emergency Fund (3 Months)",
                targetAmount = 30000.0,
                currentAmount = 12500.0,
                targetDateEpochMillis = now + (90L * dayMs),
                category = "Emergency",
                note = "Safety net for unexpected medical/job transition"
            ),
            SavingsGoalEntity(
                title = "Eid Ul-Fitr Family Gift",
                targetAmount = 15000.0,
                currentAmount = 6000.0,
                targetDateEpochMillis = now + (60L * dayMs),
                category = "Family",
                note = "Buying Punjabi for father and Saree for mother"
            ),
            SavingsGoalEntity(
                title = "Monthly DPS Savings",
                targetAmount = 5000.0,
                currentAmount = 3500.0,
                targetDateEpochMillis = now + (20L * dayMs),
                category = "Investments",
                note = "Monthly bank deposit scheme"
            )
        )
        savingsGoalDao.insertAll(sampleGoals)

        // Seed Loans
        val sampleLoans = listOf(
            LoanEntity(
                title = "Mess Utility & Gas Bill Share",
                personName = "Tanvir (Mess Manager)",
                amount = 1200.0,
                paidAmount = 500.0,
                loanType = "SHORT_TERM",
                direction = "I_OWE",
                dueDateEpochMillis = now + (5L * dayMs),
                isSettled = false,
                note = "Current month gas & cook salary share"
            ),
            LoanEntity(
                title = "Rickshaw & Metro Fare Borrowed",
                personName = "Shakil (Colleague)",
                amount = 350.0,
                paidAmount = 0.0,
                loanType = "SHORT_TERM",
                direction = "OWED_TO_ME",
                dueDateEpochMillis = now + (3L * dayMs),
                isSettled = false,
                note = "Gave cash when he had Bkash issues"
            ),
            LoanEntity(
                title = "Laptop EMI / Student Credit",
                personName = "Techland Store",
                amount = 18000.0,
                paidAmount = 6000.0,
                loanType = "LONG_TERM",
                direction = "I_OWE",
                dueDateEpochMillis = now + (180L * dayMs),
                isSettled = false,
                note = "6 months installment for work laptop"
            )
        )
        loanDao.insertAll(sampleLoans)
    }
}
