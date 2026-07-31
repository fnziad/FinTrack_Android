package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.LoanEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TaskEntity
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

data class RecurringIncomeStreamItem(
    val title: String,
    val category: String,
    val frequency: String, // "Daily", "Weekly", "Monthly", "One-time"
    val baseAmount: Double,
    val projectedMonthlyInflow: Double,
    val totalLogged: Double
)

data class DashboardUiState(
    val daysUntilSalary: Int = 0,
    val initialAmount: Double = 0.0,
    val walletCash: Double = 0.0,
    val totalSpentTillToday: Double = 0.0,
    val spentToday: Double = 0.0,
    val daysLogged: Int = 0,
    val dailyAvgSpent: Double = 0.0,
    val targetAvg: Double = 0.0,
    val insightMessage: String = "Welcome to TakaKoi! Log your daily income & expenses to see smart budget pacing insights.",
    val currencySymbol: String = "৳",
    val categorySpendList: List<CategorySpendItem> = emptyList(),
    val subCategorySpendList: List<CategorySpendItem> = emptyList(),
    // Run Rate & Target Budget Metrics
    val currentRunRate: Double = 0.0,
    val requiredRunRate: Double = 0.0,
    val runRateStatus: String = "ON_TRACK", // "ON_TRACK", "WARNING", "OVER_BUDGET"
    val runRateAdvice: String = "",
    val monthlyBudgetLimit: Double = 0.0,
    val remainingBudget: Double = 0.0,
    // Savings Target Metrics
    val targetSavingsGoal: Double = 0.0,
    val projectedSavings: Double = 0.0,
    val savingsProgressPct: Float = 0f,
    // Cost Drivers
    val topCostDriverCategory: String = "",
    val topCostDriverAmount: Double = 0.0,
    val topCostDriverPercentage: Double = 0.0,
    val highestSingleTransactionDesc: String = "",
    val highestSingleTransactionAmount: Double = 0.0,
    val costDriverSuggestion: String = "",
    // Income Breakdown & Recurring Streams
    val totalIncome: Double = 0.0,
    val recurringIncomeTotal: Double = 0.0,
    val recurringIncomeStreams: List<RecurringIncomeStreamItem> = emptyList(),
    val totalProjectedMonthlyInflow: Double = 0.0
)

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = ExpenseRepository(
        db.transactionDao(),
        db.savingsGoalDao(),
        db.loanDao(),
        db.userSettingsDao(),
        db.taskDao()
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

    val allTasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.seedSampleDataIfEmpty()
        }
    }

    val dashboardUiState: StateFlow<DashboardUiState> = combine(
        repository.userSettings,
        repository.allTransactions
    ) { settings, transactions ->
        val currSymbol = settings?.currencySymbol ?: "৳"
        val initialCash = settings?.initialCash ?: 0.0
        val salaryDay = settings?.salaryDay ?: 1
        val targetSavingsGoal = settings?.targetSavings ?: 0.0
        val targetBudgetSetting = settings?.targetBudget ?: 0.0

        val cal = Calendar.getInstance()
        val todayDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val daysRemainingInMonth = (maxDaysInMonth - todayDayOfMonth + 1).coerceAtLeast(1)

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
        var recurringIncomeTotal = 0.0
        val loggedDatesSet = mutableSetOf<String>()

        val categoryMap = mutableMapOf<String, Double>()
        val subCategoryMap = mutableMapOf<String, Double>()

        var highestTxDesc = ""
        var highestTxAmount = 0.0

        val incomeStreamsMap = mutableMapOf<String, RecurringIncomeStreamItem>()

        transactions.forEach { tx ->
            val txDateStr = dateFormat.format(Date(tx.dateEpochMillis))
            loggedDatesSet.add(txDateStr)

            if (tx.type == "EXPENSE") {
                spentTillToday += tx.amount
                if (txDateStr == todayStr) {
                    spentToday += tx.amount
                }

                if (tx.amount > highestTxAmount) {
                    highestTxAmount = tx.amount
                    highestTxDesc = "${tx.category} - ${tx.subCategory.ifEmpty { tx.description }}"
                }

                val cat = tx.category.ifEmpty { "Other" }
                categoryMap[cat] = (categoryMap[cat] ?: 0.0) + tx.amount

                val subCat = tx.subCategory.ifEmpty { "General" }
                subCategoryMap[subCat] = (subCategoryMap[subCat] ?: 0.0) + tx.amount
            } else if (tx.type == "INCOME") {
                totalIncome += tx.amount
                if (tx.isRecurring) {
                    recurringIncomeTotal += tx.amount
                }

                val freq = if (tx.isRecurring) tx.recurringFrequency.ifEmpty { "Monthly" } else "One-time"
                val title = if (tx.subCategory.isNotBlank()) tx.subCategory else tx.description.ifBlank { tx.category }
                val key = "$title-$freq"

                val projMonthly = when (freq) {
                    "Daily" -> tx.amount * maxDaysInMonth
                    "Weekly" -> tx.amount * 4.33
                    "Monthly" -> tx.amount
                    else -> tx.amount
                }

                val existing = incomeStreamsMap[key]
                if (existing == null) {
                    incomeStreamsMap[key] = RecurringIncomeStreamItem(
                        title = title,
                        category = tx.category.ifEmpty { "Income" },
                        frequency = freq,
                        baseAmount = tx.amount,
                        projectedMonthlyInflow = projMonthly,
                        totalLogged = tx.amount
                    )
                } else {
                    incomeStreamsMap[key] = existing.copy(
                        totalLogged = existing.totalLogged + tx.amount,
                        baseAmount = tx.amount,
                        projectedMonthlyInflow = projMonthly
                    )
                }
            }
        }

        val recurringStreamsList = incomeStreamsMap.values.toList()
        val totalProjectedMonthlyInflow = initialCash + recurringStreamsList.sumOf { it.projectedMonthlyInflow }

        val walletCash = initialCash + totalIncome - spentTillToday
        val daysLogged = loggedDatesSet.size
        val daysElapsed = todayDayOfMonth.coerceAtLeast(1)
        val currentRunRate = if (daysElapsed > 0) spentTillToday / daysElapsed.toDouble() else 0.0

        // Target budget calculation
        val effectiveMonthlyBudget = if (targetBudgetSetting > 0) {
            targetBudgetSetting
        } else {
            (initialCash + totalIncome - targetSavingsGoal).coerceAtLeast(0.0)
        }

        val remainingBudget = (effectiveMonthlyBudget - spentTillToday).coerceAtLeast(0.0)
        val requiredRunRate = remainingBudget / daysRemainingInMonth.toDouble()

        // Run rate status & advice
        val runRateStatus = if (currentRunRate <= requiredRunRate || requiredRunRate == 0.0) {
            "ON_TRACK"
        } else if (currentRunRate > requiredRunRate * 1.3) {
            "OVER_BUDGET"
        } else {
            "WARNING"
        }

        val runRateAdvice = when (runRateStatus) {
            "ON_TRACK" -> "🎯 On Track! Your current daily pace ($currSymbol${currentRunRate.toInt()}/day) is well within your target limit ($currSymbol${requiredRunRate.toInt()}/day). You are on course to reach your savings goal!"
            "WARNING" -> "⚠️ Caution: Your current daily pace ($currSymbol${currentRunRate.toInt()}/day) is slightly above your target daily pace ($currSymbol${requiredRunRate.toInt()}/day). Trim minor daily expenses."
            else -> "🚨 Pace Warning! You are spending $currSymbol${currentRunRate.toInt()}/day vs max allowed pace of $currSymbol${requiredRunRate.toInt()}/day. Slow down spending to avoid exhausting your budget before payday!"
        }

        // Target Savings calculation
        val projectedSavings = walletCash
        val savingsProgressPct = if (targetSavingsGoal > 0) {
            (projectedSavings / targetSavingsGoal).coerceIn(0.0, 1.0).toFloat()
        } else {
            1.0f
        }

        // Cost drivers analysis
        val topCategoryEntry = categoryMap.maxByOrNull { it.value }
        val topCategoryName = topCategoryEntry?.key ?: "None"
        val topCategoryAmount = topCategoryEntry?.value ?: 0.0
        val topCategoryPct = if (spentTillToday > 0) (topCategoryAmount / spentTillToday) * 100.0 else 0.0

        val costDriverSuggestion = if (topCategoryAmount > 0) {
            "Your #1 expense driver is $topCategoryName ($currSymbol${topCategoryAmount.toInt()}, ${topCategoryPct.toInt()}% of total expenses). Reducing this by 10% frees up $currSymbol${(topCategoryAmount * 0.1).toInt()} towards your savings goal!"
        } else {
            "Log your expenses to discover your top cost drivers and get personalized saving suggestions."
        }

        val targetAvg = if (effectiveMonthlyBudget > 0) effectiveMonthlyBudget / maxDaysInMonth.toDouble() else 0.0

        // Top spent category insight message
        val insightMsg = if (transactions.isEmpty()) {
            "No transactions logged yet. Set up your starting budget or load sample demo data to get started!"
        } else if (topCategoryEntry != null) {
            "Top cost driver: ${topCategoryEntry.key} ($currSymbol${topCategoryEntry.value.toInt()}) • ${topCategoryPct.toInt()}% of total expenses."
        } else {
            "You have spent ${currSymbol}0 so far this month."
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
            dailyAvgSpent = currentRunRate,
            targetAvg = targetAvg,
            insightMessage = insightMsg,
            currencySymbol = currSymbol,
            categorySpendList = catList,
            subCategorySpendList = subCatList,
            currentRunRate = currentRunRate,
            requiredRunRate = requiredRunRate,
            runRateStatus = runRateStatus,
            runRateAdvice = runRateAdvice,
            monthlyBudgetLimit = effectiveMonthlyBudget,
            remainingBudget = remainingBudget,
            targetSavingsGoal = targetSavingsGoal,
            projectedSavings = projectedSavings,
            savingsProgressPct = savingsProgressPct,
            topCostDriverCategory = topCategoryName,
            topCostDriverAmount = topCategoryAmount,
            topCostDriverPercentage = topCategoryPct,
            highestSingleTransactionDesc = highestTxDesc,
            highestSingleTransactionAmount = highestTxAmount,
            costDriverSuggestion = costDriverSuggestion,
            totalIncome = totalIncome,
            recurringIncomeTotal = recurringIncomeTotal,
            recurringIncomeStreams = recurringStreamsList,
            totalProjectedMonthlyInflow = totalProjectedMonthlyInflow
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    // Transaction CRUD
    fun addTransaction(
        type: String,
        amount: Double,
        category: String,
        subCategory: String,
        description: String,
        dateMillis: Long = System.currentTimeMillis(),
        isRecurring: Boolean = false,
        recurringFrequency: String = "One-time"
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
                dayName = dayName,
                isRecurring = isRecurring,
                recurringFrequency = recurringFrequency
            )
            repository.insertTransaction(tx)
        }
    }

    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
        }
    }

    // Savings CRUD
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

    // Loans CRUD
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

    // Tasks CRUD
    fun addTask(
        title: String,
        category: String = "General",
        dueDate: String = "",
        priority: String = "Medium"
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                category = category,
                dueDate = dueDate,
                priority = priority,
                isCompleted = false
            )
            repository.insertTask(task)
        }
    }

    fun toggleTaskCompleted(task: TaskEntity) {
        viewModelScope.launch {
            val updated = task.copy(isCompleted = !task.isCompleted)
            repository.updateTask(updated)
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    // User Settings & Presets
    fun updateUserSettings(
        userName: String,
        initialCash: Double,
        salaryDay: Int,
        currencySymbol: String,
        targetSavings: Double = 0.0,
        targetBudget: Double = 0.0,
        incomeFrequency: String = "Monthly",
        isDarkMode: Boolean? = null
    ) {
        viewModelScope.launch {
            val current = repository.userSettings.firstOrNull() ?: UserSettingsEntity()
            val updated = current.copy(
                userName = userName,
                initialCash = initialCash,
                salaryDay = salaryDay,
                currencySymbol = currencySymbol,
                targetSavings = targetSavings,
                targetBudget = targetBudget,
                incomeFrequency = incomeFrequency,
                isDarkMode = isDarkMode ?: current.isDarkMode
            )
            repository.saveUserSettings(updated)
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            val current = repository.userSettings.firstOrNull() ?: UserSettingsEntity()
            val updated = current.copy(isDarkMode = isDark)
            repository.saveUserSettings(updated)
        }
    }

    fun loadDemoData() {
        viewModelScope.launch {
            repository.restoreSampleData("DEMO")
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllDataAndResetSettings()
        }
    }
}
