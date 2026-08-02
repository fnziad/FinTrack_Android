package com.example.shared.ui.cashflow

import com.example.shared.data.model.AccountEntity
import com.example.shared.data.model.SpendingPlanEntity
import com.example.shared.data.model.TransactionEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

enum class AppDestination { HOME, ACTIVITY, PLAN }
enum class CaptureKind { EXPENSE, INCOME }

data class CaptureDraft(
    val kind: CaptureKind = CaptureKind.EXPENSE,
    val amount: String = "",
    val category: String = "Everyday",
    val note: String = ""
)

data class AccountBalance(val account: AccountEntity, val balance: Double)

data class PlanProgress(
    val name: String,
    val limit: Double,
    val spent: Double,
    val remaining: Double,
    val daysRemaining: Int,
    val safeToSpendToday: Double
)

object CashflowCalculations {
    fun balances(accounts: List<AccountEntity>, transactions: List<TransactionEntity>): List<AccountBalance> {
        val totals = accounts.associate { it.id to it.openingBalance }.toMutableMap()
        transactions.filter { it.status == "COMPLETED" }.forEach { transaction ->
            when (transaction.type) {
                "INCOME" -> transaction.accountId?.let { totals[it] = (totals[it] ?: 0.0) + transaction.amount }
                "EXPENSE" -> transaction.accountId?.let { totals[it] = (totals[it] ?: 0.0) - transaction.amount }
                "TRANSFER" -> {
                    transaction.accountId?.let { totals[it] = (totals[it] ?: 0.0) - transaction.amount }
                    transaction.destinationAccountId?.let { totals[it] = (totals[it] ?: 0.0) + transaction.amount }
                }
            }
        }
        return accounts.map { AccountBalance(it, totals[it.id] ?: it.openingBalance) }
    }

    fun pendingCount(transactions: List<TransactionEntity>) = transactions.count { it.status == "PENDING_SOURCE" }

    fun activePlanProgress(
        plan: SpendingPlanEntity?,
        transactions: List<TransactionEntity>,
        now: Instant = Clock.System.now(),
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): PlanProgress? {
        plan ?: return null
        val localNow = now.toLocalDateTime(zone)
        val dayMillis = 86_400_000L
        val monthLength = when (localNow.monthNumber) {
            2 -> if (localNow.year % 4 == 0 && (localNow.year % 100 != 0 || localNow.year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        val (start, end) = when (plan.cycleType) {
            "WEEKLY" -> {
                val elapsed = localNow.dayOfWeek.ordinal
                val startOfWeek = Instant.fromEpochMilliseconds(now.toEpochMilliseconds() - elapsed * dayMillis)
                startOfWeek to Instant.fromEpochMilliseconds(startOfWeek.toEpochMilliseconds() + 7L * dayMillis)
            }
            "PAYDAY" -> {
                val anchor = plan.anchorDayOfMonth.coerceIn(1, 28)
                val elapsed = if (localNow.dayOfMonth >= anchor) localNow.dayOfMonth - anchor else localNow.dayOfMonth + (monthLength - anchor)
                val startOfCycle = Instant.fromEpochMilliseconds(now.toEpochMilliseconds() - elapsed * dayMillis)
                startOfCycle to Instant.fromEpochMilliseconds(startOfCycle.toEpochMilliseconds() + monthLength * dayMillis)
            }
            "CUSTOM" -> {
                val customStart = plan.customStartEpochMillis.takeIf { it > 0 } ?: now.toEpochMilliseconds()
                val customEnd = plan.customEndEpochMillis.takeIf { it > customStart } ?: customStart + 30L * dayMillis
                Instant.fromEpochMilliseconds(customStart) to Instant.fromEpochMilliseconds(customEnd)
            }
            else -> {
                val startOfMonth = Instant.fromEpochMilliseconds(now.toEpochMilliseconds() - (localNow.dayOfMonth - 1L) * dayMillis)
                startOfMonth to Instant.fromEpochMilliseconds(startOfMonth.toEpochMilliseconds() + monthLength * dayMillis)
            }
        }
        val spent = transactions.filter { it.type == "EXPENSE" && it.dateEpochMillis in start.toEpochMilliseconds()..end.toEpochMilliseconds() }
            .sumOf { it.amount }
        val daysRemaining = localNow.date.daysUntil(end.toLocalDateTime(zone).date).coerceAtLeast(1)
        val remaining = (plan.limitAmount - spent).coerceAtLeast(0.0)
        return PlanProgress(plan.name, plan.limitAmount, spent, remaining, daysRemaining, remaining / daysRemaining)
    }
}
