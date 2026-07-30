package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.LoanEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserSettingsEntity
import com.example.data.repository.ExpenseRepository
import com.example.ui.components.CategorySpendItem
import com.example.ui.components.DefaultCategoryColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DashboardUiState(
    val daysUntilSalary: Int = 26,
    val initialAmount: Double = 25000.0,
    val walletCash: Double = 25000.0,
    val totalSpentTillToday: Double = 0.0,
    val spentToday: Double = 0.0,
    val daysLogged: Int = 1,
    val dailyAvgSpent: Double = 0.0,
    val targetAvg: Double = 833.3,
    val insightMessage: String = "Welcome to FinTrack! Log your daily expenses to see smart insights.",
    val currencySymbol: String = "৳",
    val categorySpendList: List<CategorySpendItem> = emptyList(),
    val subCategorySpendList: List<CategorySpendItem> = emptyList()
)

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = ExpenseRepository(
        db.transactionDao(),
        db.savingsGoalDao(),
        db.loanDao(),
        db.userSettingsDao()
    )

    val userSettings: StateFlow<UserSettingsEntity?> = repository.userSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allSavingsGoals: StateFlow<List<SavingsGoalEntity>> = repository.allSavingsGoals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allLoans: StateFlow<List<LoanEntity>> = repository.allLoans
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            val settings = repository.userSettings.firstOrNull()
            if (settings == null || !settings.isDataLoaded) {
                repository.seedSampleDataIfEmpty("FRESHER")
            }
        }
    }

    val dashboardUiState: StateFlow<DashboardUiState> = combine(
        repository.userSettings,
        repository.allTransactions
    ) { settings, transactions ->
        val currSymbol = settings?.currencySymbol ?: "৳"
        val initialCash = settings?.initialCash ?: 25000.0
        val salaryDay = settings?.salaryDay ?: 1

        val cal = Calendar.getInstance()
        val todayDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val daysUntilSalary = if (salaryDay > todayDayOfMonth) {
            salaryDay - todayDayOfMonth
        } else if (salaryDay == todayDayOfMonth) {
            0
        } else {
            (maxDaysInMonth - todayDayOfMonth) + salaryDay
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = dateFormat.format(Date())

        var spentTillToday = 0.0
        var spentToday = 0.0
        var totalIncome = 0.0
        val loggedDatesSet = mutableSetOf<String>()

        val categoryMap = mutableMapOf<String, Double>()
        val subCategoryMap = mutableMapOf<String, Double>()

        transactions.forEach { tx ->
            val txDateStr = dateFormat.format(Date(tx.dateEpochMillis))
            loggedDatesSet.add(txDateStr)

            if (tx.type == "EXPENSE") {
                spentTillToday += tx.amount
                if (txDateStr == todayStr) {
                    spentToday += tx.amount
                }

                val cat = tx.category.ifEmpty { "Other" }
                categoryMap[cat] = (categoryMap[cat] ?: 0.0) + tx.amount

                val subCat = tx.subCategory.ifEmpty { "General" }
                subCategoryMap[subCat] = (subCategoryMap[subCat] ?: 0.0) + tx.amount
            } else if (tx.type == "INCOME") {
                totalIncome += tx.amount
            }
        }

        val walletCash = initialCash + totalIncome - spentTillToday
        val daysLogged = loggedDatesSet.size.coerceAtLeast(1)
        val dailyAvgSpent = spentTillToday / daysLogged.toDouble()
        val targetAvg = initialCash / maxDaysInMonth.toDouble()

        // Top spent category insight message
        val topCategoryEntry = categoryMap.maxByOrNull { it.value }
        val insightMsg = if (topCategoryEntry != null) {
            "You spent the most on ${topCategoryEntry.key} this month ($currSymbol${topCategoryEntry.value.toInt()})"
        } else {
            "You spent ৳0 so far. Log your daily meals, rickshaw fares, and bills!"
        }

        // Convert maps to sorted list for bar charts
        val catList = categoryMap.entries
            .sortedByDescending { it.value }
            .mapIndexed { idx, entry ->
                CategorySpendItem(
                    name = entry.key,
                    amount = entry.value,
                    color = DefaultCategoryColors.getOrElse(idx) { DefaultCategoryColors.last() }
                )
            }

        val subCatList = subCategoryMap.entries
            .sortedByDescending { it.value }
            .mapIndexed { idx, entry ->
                CategorySpendItem(
                    name = entry.key,
                    amount = entry.value,
                    color = DefaultCategoryColors.getOrElse(idx % DefaultCategoryColors.size) { DefaultCategoryColors.first() }
                )
            }

        DashboardUiState(
            daysUntilSalary = daysUntilSalary,
            initialAmount = initialCash,
            walletCash = walletCash,
            totalSpentTillToday = spentTillToday,
            spentToday = spentToday,
            daysLogged = daysLogged,
            dailyAvgSpent = dailyAvgSpent,
            targetAvg = targetAvg,
            insightMessage = insightMsg,
            currencySymbol = currSymbol,
            categorySpendList = catList,
            subCategorySpendList = subCatList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    // CRUD Actions
    fun addTransaction(
        type: String,
        amount: Double,
        category: String,
        subCategory: String,
        description: String,
        dateMillis: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            val cal = Calendar.getInstance().apply { timeInMillis = dateMillis }
            val dayName = SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(cal.time)

            val tx = TransactionEntity(
                type = type,
                amount = amount,
                category = category,
                subCategory = subCategory,
                description = description,
                dateEpochMillis = dateMillis,
                dayName = dayName
            )
            repository.insertTransaction(tx)
        }
    }

    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
        }
    }

    fun addSavingsGoal(
        title: String,
        targetAmount: Double,
        currentAmount: Double,
        category: String,
        note: String
    ) {
        viewModelScope.launch {
            val goal = SavingsGoalEntity(
                title = title,
                targetAmount = targetAmount,
                currentAmount = currentAmount,
                category = category,
                note = note
            )
            repository.insertSavingsGoal(goal)
        }
    }

    fun updateSavingsAmount(goal: SavingsGoalEntity, addedAmount: Double) {
        viewModelScope.launch {
            val updated = goal.copy(currentAmount = (goal.currentAmount + addedAmount).coerceAtLeast(0.0))
            repository.updateSavingsGoal(updated)
        }
    }

    fun deleteSavingsGoal(id: Int) {
        viewModelScope.launch {
            repository.deleteSavingsGoal(id)
        }
    }

    fun addLoan(
        title: String,
        personName: String,
        amount: Double,
        loanType: String,
        direction: String,
        note: String
    ) {
        viewModelScope.launch {
            val loan = LoanEntity(
                title = title,
                personName = personName,
                amount = amount,
                loanType = loanType,
                direction = direction,
                note = note
            )
            repository.insertLoan(loan)
        }
    }

    fun repayLoan(loan: LoanEntity, repaymentAmount: Double) {
        viewModelScope.launch {
            val newPaid = loan.paidAmount + repaymentAmount
            val isSettled = newPaid >= loan.amount
            val updated = loan.copy(paidAmount = newPaid, isSettled = isSettled)
            repository.updateLoan(updated)
        }
    }

    fun deleteLoan(id: Int) {
        viewModelScope.launch {
            repository.deleteLoan(id)
        }
    }

    fun updateProfilePreset(preset: String) {
        viewModelScope.launch {
            repository.restoreSampleData(preset = preset, keepUserSettings = false)
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            val current = repository.userSettings.firstOrNull() ?: UserSettingsEntity()
            val updated = current.copy(isDarkMode = isDark)
            repository.saveUserSettings(updated)
        }
    }

    fun updateUserSettings(
        userName: String,
        initialCash: Double,
        salaryDay: Int,
        currencySymbol: String,
        isDarkMode: Boolean? = null
    ) {
        viewModelScope.launch {
            val current = repository.userSettings.firstOrNull() ?: UserSettingsEntity()
            val updated = current.copy(
                userName = userName,
                initialCash = initialCash,
                salaryDay = salaryDay,
                currencySymbol = currencySymbol,
                isDarkMode = isDarkMode ?: current.isDarkMode
            )
            repository.saveUserSettings(updated)
        }
    }

    fun clearAllUserData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    fun resetData() {
        viewModelScope.launch {
            val currentPreset = repository.userSettings.firstOrNull()?.profileType ?: "FRESHER"
            repository.restoreSampleData(preset = currentPreset, keepUserSettings = true)
        }
    }
}
