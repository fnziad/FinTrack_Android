package com.example.data.repository

import com.example.data.dao.LoanDao
import com.example.data.dao.SavingsGoalDao
import com.example.data.dao.TaskDao
import com.example.data.dao.TransactionDao
import com.example.data.dao.UserSettingsDao
import com.example.data.model.LoanEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserSettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ExpenseRepository(
    private val transactionDao: TransactionDao,
    private val savingsGoalDao: SavingsGoalDao,
    private val loanDao: LoanDao,
    private val userSettingsDao: UserSettingsDao,
    private val taskDao: TaskDao
) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val allSavingsGoals: Flow<List<SavingsGoalEntity>> = savingsGoalDao.getAllSavingsGoals()
    val allLoans: Flow<List<LoanEntity>> = loanDao.getAllLoans()
    val userSettings: Flow<UserSettingsEntity?> = userSettingsDao.getUserSettings()
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun insertTransaction(transaction: TransactionEntity) = transactionDao.insertTransaction(transaction)
    suspend fun updateTransaction(transaction: TransactionEntity) = transactionDao.updateTransaction(transaction)
    suspend fun deleteTransaction(id: Int) = transactionDao.deleteTransactionById(id)

    suspend fun insertSavingsGoal(goal: SavingsGoalEntity) = savingsGoalDao.insertSavingsGoal(goal)
    suspend fun updateSavingsGoal(goal: SavingsGoalEntity) = savingsGoalDao.updateSavingsGoal(goal)
    suspend fun deleteSavingsGoal(id: Int) = savingsGoalDao.deleteSavingsGoalById(id)

    suspend fun insertLoan(loan: LoanEntity) = loanDao.insertLoan(loan)
    suspend fun updateLoan(loan: LoanEntity) = loanDao.updateLoan(loan)
    suspend fun deleteLoan(id: Int) = loanDao.deleteLoanById(id)

    suspend fun insertTask(task: TaskEntity) = taskDao.insertTask(task)
    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)
    suspend fun deleteTask(id: Int) = taskDao.deleteTaskById(id)

    suspend fun saveUserSettings(settings: UserSettingsEntity) = userSettingsDao.saveUserSettings(settings)

    suspend fun clearAllData() {
        transactionDao.deleteAllTransactions()
        savingsGoalDao.deleteAll()
        loanDao.deleteAll()
        taskDao.deleteAllTasks()
    }

    suspend fun clearAllDataAndResetSettings() {
        clearAllData()
        userSettingsDao.saveUserSettings(
            UserSettingsEntity(
                id = 1,
                profileType = "CUSTOM",
                userName = "",
                initialCash = 0.0,
                salaryDay = 1,
                currencySymbol = "৳",
                targetSavings = 0.0,
                targetBudget = 0.0,
                incomeFrequency = "Monthly",
                isDarkMode = false,
                isDataLoaded = true
            )
        )
    }

    suspend fun seedSampleDataIfEmpty() {
        val current = userSettingsDao.getUserSettings().firstOrNull()
        if (current == null) {
            userSettingsDao.saveUserSettings(
                UserSettingsEntity(
                    id = 1,
                    profileType = "CUSTOM",
                    userName = "",
                    initialCash = 0.0,
                    salaryDay = 1,
                    currencySymbol = "৳",
                    targetSavings = 0.0,
                    targetBudget = 0.0,
                    incomeFrequency = "Monthly",
                    isDarkMode = false,
                    isDataLoaded = true
                )
            )
        }
    }

    suspend fun restoreSampleData(preset: String = "DEMO") {
        val now = System.currentTimeMillis()
        val dayMs = 24L * 3600 * 1000

        val settings = UserSettingsEntity(
            id = 1,
            profileType = preset,
            userName = "Demo User",
            initialCash = 25000.0,
            salaryDay = 1,
            currencySymbol = "৳",
            targetSavings = 5000.0,
            targetBudget = 18000.0,
            incomeFrequency = "Monthly",
            isDarkMode = false,
            isDataLoaded = true
        )
        userSettingsDao.saveUserSettings(settings)

        clearAllData()

        // Seed sample transactions including recurring income
        val sampleTransactions = listOf(
            TransactionEntity(
                type = "INCOME",
                amount = 25000.0,
                category = "Salary & Income",
                subCategory = "Monthly Salary",
                description = "Tech Solutions Ltd. Salary",
                dateEpochMillis = now - (15 * dayMs),
                dayName = "1st",
                isRecurring = true,
                recurringFrequency = "Monthly"
            ),
            TransactionEntity(
                type = "INCOME",
                amount = 1500.0,
                category = "Pocket Money & Stipend",
                subCategory = "Weekly Allowance",
                description = "Weekly pocket money received",
                dateEpochMillis = now - (7 * dayMs),
                dayName = "7th",
                isRecurring = true,
                recurringFrequency = "Weekly"
            ),
            TransactionEntity(
                type = "INCOME",
                amount = 800.0,
                category = "Business & Side Hustle",
                subCategory = "Daily Shop Sales",
                description = "Daily sales profit from small shop / online store",
                dateEpochMillis = now - (2 * dayMs),
                dayName = "2 days ago",
                isRecurring = true,
                recurringFrequency = "Daily"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 8000.0,
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
                description = "30-day data pack + talktime",
                dateEpochMillis = now - (6 * dayMs),
                dayName = "10th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 150.0,
                category = "Utilities & Mobile",
                subCategory = "Cashout Fee",
                description = "Mobile wallet cashout fee",
                dateEpochMillis = now - (4 * dayMs),
                dayName = "12th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 2500.0,
                category = "Education & Tuition",
                subCategory = "Tuition / Course Fee",
                description = "Skill development online course",
                dateEpochMillis = now - (3 * dayMs),
                dayName = "13th"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 1800.0,
                category = "Shopping & Personal",
                subCategory = "Clothes & Shoes",
                description = "Formal shirt from market",
                dateEpochMillis = now - (1 * dayMs),
                dayName = "Yesterday"
            ),
            TransactionEntity(
                type = "EXPENSE",
                amount = 180.0,
                category = "Food & Mess",
                subCategory = "Tea Stall & Snacks",
                description = "Raw tea + Snacks",
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
                note = "Safety net for unexpected medical/transition"
            ),
            SavingsGoalEntity(
                title = "Family Festival Gift",
                targetAmount = 15000.0,
                currentAmount = 6000.0,
                targetDateEpochMillis = now + (60L * dayMs),
                category = "Family",
                note = "Buying gifts for parents"
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
                title = "Mess Utility & Gas Share",
                personName = "Tanvir (Mess Manager)",
                amount = 1200.0,
                paidAmount = 500.0,
                loanType = "SHORT_TERM",
                direction = "I_OWE",
                dueDateEpochMillis = now + (5L * dayMs),
                isSettled = false,
                note = "Gas & cook salary share"
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
                note = "Cash borrowed during app downtime"
            )
        )
        loanDao.insertAll(sampleLoans)

        // Seed Sample Financial Tasks
        val sampleTasks = listOf(
            TaskEntity(
                title = "Pay monthly internet & Wi-Fi bill",
                category = "Bills",
                dueDate = "In 3 days",
                priority = "High",
                isCompleted = false
            ),
            TaskEntity(
                title = "Deposit ৳3,500 into Monthly DPS",
                category = "Savings",
                dueDate = "15th of Month",
                priority = "High",
                isCompleted = false
            ),
            TaskEntity(
                title = "Repay ৳700 remaining mess share to Tanvir",
                category = "Loans",
                dueDate = "20th of Month",
                priority = "Medium",
                isCompleted = false
            ),
            TaskEntity(
                title = "Top up Metro Rail Pass (MRT)",
                category = "Transport",
                dueDate = "This weekend",
                priority = "Low",
                isCompleted = true
            )
        )
        sampleTasks.forEach { taskDao.insertTask(it) }
    }
}
