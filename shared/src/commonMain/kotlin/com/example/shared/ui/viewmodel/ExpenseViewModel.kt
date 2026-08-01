package com.example.shared.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.database.AppDatabase
import com.example.shared.data.database.buildAppDatabase
import com.example.shared.data.database.getDatabaseBuilder
import com.example.shared.data.model.LoanEntity
import com.example.shared.data.model.SavingsGoalEntity
import com.example.shared.data.model.TaskEntity
import com.example.shared.data.model.TransactionEntity
import com.example.shared.data.model.UserSettingsEntity
import com.example.shared.data.repository.ExpenseRepository
import com.example.shared.ui.components.CategorySpendItem
import com.example.shared.ui.components.DefaultCategoryColors
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class RecurringIncomeStreamItem(
    val title: String,
    val category: String,
    val frequency: String,
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
    val currentRunRate: Double = 0.0,
    val requiredRunRate: Double = 0.0,
    val runRateStatus: String = "ON_TRACK",
    val runRateAdvice: String = "",
    val monthlyBudgetLimit: Double = 0.0,
    val remainingBudget: Double = 0.0,
    val targetSavingsGoal: Double = 0.0,
    val projectedSavings: Double = 0.0,
    val savingsProgressPct: Float = 0f,
    val topCostDriverCategory: String = "",
    val topCostDriverAmount: Double = 0.0,
    val topCostDriverPercentage: Double = 0.0,
    val highestSingleTransactionDesc: String = "",
    val highestSingleTransactionAmount: Double = 0.0,
    val costDriverSuggestion: String = "",
    val totalIncome: Double = 0.0,
    val recurringIncomeTotal: Double = 0.0,
    val recurringIncomeStreams: List<RecurringIncomeStreamItem> = emptyList(),
    val totalProjectedMonthlyInflow: Double = 0.0
)

class ExpenseViewModel : ViewModel() {
    private val db: AppDatabase = buildAppDatabase(getDatabaseBuilder())
    private val repository = ExpenseRepository(
        db.transactionDao(),
        db.savingsGoalDao(),
        db.loanDao(),
        db.userSettingsDao(),
        db.taskDao()
    )

    val userSettings: StateFlow<UserSettingsEntity?> = repository.userSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSavingsGoals: StateFlow<List<SavingsGoalEntity>> = repository.allSavingsGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLoans: StateFlow<List<LoanEntity>> = repository.allLoans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch { repository.seedSampleDataIfEmpty() }
    }

    val dashboardUiState: StateFlow<DashboardUiState> = combine(
        repository.userSettings,
        repository.allTransactions
    ) { settings: UserSettingsEntity?, transactions: List<TransactionEntity> ->
        computeDashboardState(settings, transactions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    private fun computeDashboardState(
        settings: UserSettingsEntity?,
        transactions: List<TransactionEntity>
    ): DashboardUiState {
        val currSymbol = settings?.currencySymbol ?: "৳"
        val initialCash = settings?.initialCash ?: 0.0
        val salaryDay = settings?.salaryDay ?: 1
        val targetSavingsGoal = settings?.targetSavings ?: 0.0
        val targetBudgetSetting = settings?.targetBudget ?: 0.0

        val tz = TimeZone.currentSystemDefault()
        val localDate = Clock.System.now().toLocalDateTime(tz)
        val todayDayOfMonth = localDate.dayOfMonth
        val monthNum = localDate.monthNumber
        val yearNum = localDate.year

        val maxDaysInMonth = when (monthNum) {
            2 -> if (yearNum % 4 == 0 && (yearNum % 100 != 0 || yearNum % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }

        val daysRemainingInMonth = (maxDaysInMonth - todayDayOfMonth + 1).coerceAtLeast(1)
        val todayStr = "${yearNum}-${monthNum.toString().padStart(2, '0')}-${todayDayOfMonth.toString().padStart(2, '0')}"

        val daysUntilSalary = when {
            salaryDay > todayDayOfMonth -> salaryDay - todayDayOfMonth
            salaryDay == todayDayOfMonth -> 0
            else -> (maxDaysInMonth - todayDayOfMonth) + salaryDay
        }

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
            val txDate = Instant.fromEpochMilliseconds(tx.dateEpochMillis).toLocalDateTime(tz)
            val txDateStr = "${txDate.year}-${txDate.monthNumber.toString().padStart(2, '0')}-${txDate.dayOfMonth.toString().padStart(2, '0')}"
            loggedDatesSet.add(txDateStr)

            if (tx.type == "EXPENSE") {
                spentTillToday += tx.amount
                if (txDateStr == todayStr) spentToday += tx.amount
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
                if (tx.isRecurring) recurringIncomeTotal += tx.amount
                val freq = if (tx.isRecurring) tx.recurringFrequency.ifEmpty { "Monthly" } else "One-time"
                val title = if (tx.subCategory.isNotBlank()) tx.subCategory else tx.description.ifBlank { tx.category }
                val key = "$title-$freq"
                val projMonthly = when (freq) {
                    "Daily" -> tx.amount * maxDaysInMonth
                    "Weekly" -> tx.amount * 4.33
                    else -> tx.amount
                }
                val existing = incomeStreamsMap[key]
                incomeStreamsMap[key] = existing?.copy(
                    totalLogged = existing.totalLogged + tx.amount,
                    baseAmount = tx.amount,
                    projectedMonthlyInflow = projMonthly
                ) ?: RecurringIncomeStreamItem(title, tx.category.ifEmpty { "Income" }, freq, tx.amount, projMonthly, tx.amount)
            }
        }

        val recurringStreamsList = incomeStreamsMap.values.toList()
        val walletCash = initialCash + totalIncome - spentTillToday
        val daysLogged = loggedDatesSet.size
        val daysElapsed = todayDayOfMonth.coerceAtLeast(1)
        val currentRunRate = if (daysElapsed > 0) spentTillToday / daysElapsed.toDouble() else 0.0

        val effectiveMonthlyBudget = if (targetBudgetSetting > 0) targetBudgetSetting
        else (initialCash + totalIncome - targetSavingsGoal).coerceAtLeast(0.0)

        val remainingBudget = (effectiveMonthlyBudget - spentTillToday).coerceAtLeast(0.0)
        val requiredRunRate = remainingBudget / daysRemainingInMonth.toDouble()
        val runRateStatus = when {
            currentRunRate <= requiredRunRate || requiredRunRate == 0.0 -> "ON_TRACK"
            currentRunRate > requiredRunRate * 1.3 -> "OVER_BUDGET"
            else -> "WARNING"
        }
        val runRateAdvice = when (runRateStatus) {
            "ON_TRACK" -> "🎯 On Track! Your current daily pace ($currSymbol${currentRunRate.toInt()}/day) is well within your target limit ($currSymbol${requiredRunRate.toInt()}/day)."
            "WARNING" -> "⚠️ Caution: Your current daily pace ($currSymbol${currentRunRate.toInt()}/day) is slightly above your target ($currSymbol${requiredRunRate.toInt()}/day)."
            else -> "🚨 Pace Warning! Spending $currSymbol${currentRunRate.toInt()}/day vs max $currSymbol${requiredRunRate.toInt()}/day."
        }

        val projectedSavings = walletCash
        val savingsProgressPct = if (targetSavingsGoal > 0) (projectedSavings / targetSavingsGoal).coerceIn(0.0, 1.0).toFloat() else 1f
        val topEntry = categoryMap.maxByOrNull { it.value }
        val topCategoryPct = if (spentTillToday > 0) ((topEntry?.value ?: 0.0) / spentTillToday) * 100.0 else 0.0
        val targetAvg = if (effectiveMonthlyBudget > 0) effectiveMonthlyBudget / maxDaysInMonth.toDouble() else 0.0

        val catList = categoryMap.entries.sortedByDescending { it.value }.mapIndexed { idx, e ->
            CategorySpendItem(e.key, e.value, DefaultCategoryColors.getOrElse(idx) { DefaultCategoryColors.last() })
        }
        val subCatList = subCategoryMap.entries.sortedByDescending { it.value }.mapIndexed { idx, e ->
            CategorySpendItem(e.key, e.value, DefaultCategoryColors.getOrElse(idx % DefaultCategoryColors.size) { DefaultCategoryColors.first() })
        }

        return DashboardUiState(
            daysUntilSalary = daysUntilSalary,
            initialAmount = initialCash,
            walletCash = walletCash,
            totalSpentTillToday = spentTillToday,
            spentToday = spentToday,
            daysLogged = daysLogged,
            dailyAvgSpent = currentRunRate,
            targetAvg = targetAvg,
            insightMessage = if (transactions.isEmpty()) "No transactions yet. Log income & expenses to get insights!" else topEntry?.let { "Top cost driver: ${it.key} ($currSymbol${it.value.toInt()}) • ${topCategoryPct.toInt()}% of total expenses." } ?: "You have spent ${currSymbol}0 so far.",
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
            topCostDriverCategory = topEntry?.key ?: "None",
            topCostDriverAmount = topEntry?.value ?: 0.0,
            topCostDriverPercentage = topCategoryPct,
            highestSingleTransactionDesc = highestTxDesc,
            highestSingleTransactionAmount = highestTxAmount,
            costDriverSuggestion = if ((topEntry?.value ?: 0.0) > 0) "Your #1 expense driver is ${topEntry?.key} ($currSymbol${topEntry?.value?.toInt()}, ${topCategoryPct.toInt()}% of total). Reducing by 10% frees $currSymbol${((topEntry?.value ?: 0.0) * 0.1).toInt()}!" else "Log expenses to discover your top cost drivers.",
            totalIncome = totalIncome,
            recurringIncomeTotal = recurringIncomeTotal,
            recurringIncomeStreams = recurringStreamsList,
            totalProjectedMonthlyInflow = initialCash + recurringStreamsList.sumOf { it.projectedMonthlyInflow }
        )
    }

    fun addTransaction(type: String, amount: Double, category: String, subCategory: String, description: String, dateMillis: Long = Clock.System.now().toEpochMilliseconds(), isRecurring: Boolean = false, recurringFrequency: String = "One-time") {
        viewModelScope.launch {
            val tz = TimeZone.currentSystemDefault()
            val txDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(tz)
            val dayName = "${txDate.dayOfWeek.name.take(3)}, ${txDate.dayOfMonth} ${txDate.month.name.take(3)}"
            repository.insertTransaction(TransactionEntity(type = type, amount = amount, category = category, subCategory = subCategory, description = description, dateEpochMillis = dateMillis, dayName = dayName, isRecurring = isRecurring, recurringFrequency = recurringFrequency))
        }
    }

    fun deleteTransaction(id: Int) { viewModelScope.launch { repository.deleteTransaction(id) } }

    fun addSavingsGoal(title: String, targetAmount: Double, currentAmount: Double, category: String, note: String) {
        viewModelScope.launch { repository.insertSavingsGoal(SavingsGoalEntity(title = title, targetAmount = targetAmount, currentAmount = currentAmount, category = category, note = note)) }
    }

    fun updateSavingsAmount(goal: SavingsGoalEntity, addedAmount: Double) {
        viewModelScope.launch { repository.updateSavingsGoal(goal.copy(currentAmount = (goal.currentAmount + addedAmount).coerceAtLeast(0.0))) }
    }

    fun deleteSavingsGoal(id: Int) { viewModelScope.launch { repository.deleteSavingsGoal(id) } }

    fun addLoan(title: String, personName: String, amount: Double, loanType: String, direction: String, note: String) {
        viewModelScope.launch { repository.insertLoan(LoanEntity(title = title, personName = personName, amount = amount, loanType = loanType, direction = direction, note = note)) }
    }

    fun repayLoan(loan: LoanEntity, repaymentAmount: Double) {
        viewModelScope.launch {
            val newPaid = loan.paidAmount + repaymentAmount
            repository.updateLoan(loan.copy(paidAmount = newPaid, isSettled = newPaid >= loan.amount))
        }
    }

    fun deleteLoan(id: Int) { viewModelScope.launch { repository.deleteLoan(id) } }

    fun addTask(title: String, category: String = "General", dueDate: String = "", priority: String = "Medium") {
        viewModelScope.launch { repository.insertTask(TaskEntity(title = title, category = category, dueDate = dueDate, priority = priority, isCompleted = false)) }
    }

    fun toggleTaskCompleted(task: TaskEntity) { viewModelScope.launch { repository.updateTask(task.copy(isCompleted = !task.isCompleted)) } }
    fun deleteTask(id: Int) { viewModelScope.launch { repository.deleteTask(id) } }

    fun updateUserSettings(userName: String, initialCash: Double, salaryDay: Int, currencySymbol: String, targetSavings: Double = 0.0, targetBudget: Double = 0.0, incomeFrequency: String = "Monthly", colorTheme: String? = null, isDarkMode: Boolean? = null) {
        viewModelScope.launch {
            val current = repository.userSettings.firstOrNull() ?: UserSettingsEntity()
            repository.saveUserSettings(current.copy(userName = userName, initialCash = initialCash, salaryDay = salaryDay, currencySymbol = currencySymbol, targetSavings = targetSavings, targetBudget = targetBudget, incomeFrequency = incomeFrequency, colorTheme = colorTheme ?: current.colorTheme, isDarkMode = isDarkMode ?: current.isDarkMode))
        }
    }

    fun updateColorTheme(colorTheme: String) {
        viewModelScope.launch { repository.userSettings.firstOrNull()?.let { repository.saveUserSettings(it.copy(colorTheme = colorTheme)) } }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch { repository.userSettings.firstOrNull()?.let { repository.saveUserSettings(it.copy(isDarkMode = isDark)) } }
    }

    fun loadDemoData() { viewModelScope.launch { repository.restoreSampleData("DEMO") } }
    fun clearAllData() { viewModelScope.launch { repository.clearAllDataAndResetSettings() } }
}
