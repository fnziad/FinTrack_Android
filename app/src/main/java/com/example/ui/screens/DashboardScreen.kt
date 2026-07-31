package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskEntity
import com.example.ui.components.CategoryBarChart
import com.example.ui.components.DoughnutChart
import com.example.ui.theme.BentoAmber
import com.example.ui.theme.BentoAmberLight
import com.example.ui.theme.BentoCardBorder
import com.example.ui.theme.BentoDarkCard
import com.example.ui.theme.BentoEmerald
import com.example.ui.theme.BentoEmeraldLight
import com.example.ui.theme.BentoIndigoContainer
import com.example.ui.theme.BentoIndigoLight
import com.example.ui.theme.BentoIndigoPrimary
import com.example.ui.theme.BentoRose
import com.example.ui.theme.CoralExpense
import com.example.ui.viewmodel.ExpenseViewModel

@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel,
    onAddExpenseClick: () -> Unit,
    onNavigateToLedger: () -> Unit = {},
    onNavigateToLoans: () -> Unit = {},
    onNavigateToSavings: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.dashboardUiState.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val loans by viewModel.allLoans.collectAsState()
    val transactions by viewModel.allTransactions.collectAsState()
    val tasks by viewModel.allTasks.collectAsState()

    val rawProfileName = userSettings?.userName.orEmpty()
    val profileName = if (rawProfileName.isNotBlank()) rawProfileName else "My Expense Tracker"
    val symbol = uiState.currencySymbol

    val totalIOwe = loans.filter { it.direction == "I_OWE" && !it.isSettled }.sumOf { it.amount - it.paidAmount }
    val totalOwedToMe = loans.filter { it.direction == "OWED_TO_ME" && !it.isSettled }.sumOf { it.amount - it.paidAmount }

    var showProfileSetupDialog by remember { mutableStateOf(false) }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header Section - Bento Profile & Branding
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = if (rawProfileName.isNotBlank()) "WELCOME, ${profileName.uppercase()}" else "TAKAKOI BUDGET TRACKER",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "TakaKoi",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showProfileSetupDialog = true }
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BentoIndigoLight)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Setup",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BentoIndigoPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = BentoIndigoPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BentoIndigoContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = BentoIndigoPrimary
                        )
                    }
                }
            }

            // Quick Banner for Empty / Fresh State
            if (transactions.isEmpty() && uiState.initialAmount == 0.0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .border(1.dp, BentoIndigoContainer, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoIndigoLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🚀 Get Started with FinTrack",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoIndigoPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Set your custom starting budget or click below to load sample demo data and see how charts and tracking work.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showProfileSetupDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = BentoIndigoPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Set Up Profile", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { viewModel.loadDemoData() },
                                modifier = Modifier.weight(1f).testTag("btn_load_sample_demo"),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BentoIndigoPrimary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = BentoIndigoPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Load Sample Data", fontWeight = FontWeight.Bold, color = BentoIndigoPrimary, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Bento Grid Block 1: Salary Countdown (Hero Card) + Side Budget Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Hero Salary Countdown Bento Box
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showProfileSetupDialog = true }
                        .testTag("stat_days_until_salary"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoIndigoPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = BentoIndigoContainer,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Column {
                            Text(
                                text = "${uiState.daysUntilSalary}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "DAYS UNTIL PAYDAY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 0.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Column of 2 Mini Bento Cards (Initial Budget & Wallet Cash)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Initial Budget Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BentoCardBorder, RoundedCornerShape(20.dp))
                            .clickable { showProfileSetupDialog = true }
                            .testTag("stat_initial_amount"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BentoEmeraldLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = BentoEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "STARTING BUDGET",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$symbol${uiState.initialAmount.toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Wallet Cash Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BentoCardBorder, RoundedCornerShape(20.dp))
                            .clickable { onNavigateToLedger() }
                            .testTag("stat_wallet_cash"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BentoAmberLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = BentoAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "REMAINING CASH",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$symbol${uiState.walletCash.toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bento Grid Block 2: Daily Spend vs Month Total Card + Averages
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Spent Today Bento Box
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, BentoCardBorder, RoundedCornerShape(24.dp))
                        .clickable { onNavigateToLedger() }
                        .testTag("stat_spent_today"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SPENT TODAY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (uiState.spentToday > 0) Color(0xFFFFE4E6) else Color(0xFFD1FAE5))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (uiState.spentToday > 0) "Today" else "Saved",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.spentToday > 0) BentoRose else BentoEmerald
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$symbol${uiState.spentToday.toInt()}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Total Spent So Far",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$symbol${uiState.totalSpentTillToday.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoRose,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Daily Avg & Target Bento Box
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, BentoCardBorder, RoundedCornerShape(24.dp))
                        .clickable { onNavigateToLedger() }
                        .testTag("stat_daily_avg"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "DAILY AVG SPENT",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$symbol${uiState.dailyAvgSpent.toInt()}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Target / Day",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$symbol${uiState.targetAvg.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoEmerald,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // DAILY SPEND PACE & TARGET BUDGET TRACKER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoIndigoContainer, RoundedCornerShape(24.dp))
                    .testTag("stat_run_rate_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎯 DAILY SPEND PACE TRACKER",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BentoIndigoPrimary,
                                letterSpacing = 0.5.sp
                            )
                        }

                        val statusBadgeBg = when (uiState.runRateStatus) {
                            "ON_TRACK" -> BentoEmeraldLight
                            "WARNING" -> BentoAmberLight
                            else -> Color(0xFFFFE4E6)
                        }
                        val statusBadgeTxt = when (uiState.runRateStatus) {
                            "ON_TRACK" -> BentoEmerald
                            "WARNING" -> BentoAmber
                            else -> BentoRose
                        }
                        val statusText = when (uiState.runRateStatus) {
                            "ON_TRACK" -> "ON TRACK 🎯"
                            "WARNING" -> "CAUTION ⚠️"
                            else -> "HIGH PACE 🚨"
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(statusBadgeBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = statusText,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = statusBadgeTxt
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CURRENT DAILY PACE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$symbol${uiState.currentRunRate.toInt()}/day",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (uiState.runRateStatus == "OVER_BUDGET") BentoRose else BentoIndigoPrimary
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TARGET MAX PACE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$symbol${uiState.requiredRunRate.toInt()}/day",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoEmerald
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (uiState.targetSavingsGoal > 0) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Target Savings: $symbol${uiState.targetSavingsGoal.toInt()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Projected: $symbol${uiState.projectedSavings.toInt()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoEmerald
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            androidx.compose.material3.LinearProgressIndicator(
                                progress = { uiState.savingsProgressPct },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = BentoEmerald,
                                trackColor = BentoEmeraldLight
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Text(
                        text = uiState.runRateAdvice,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // TOP COST DRIVERS & SPENDING ANALYTICS
            if (uiState.topCostDriverAmount > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BentoCardBorder, RoundedCornerShape(24.dp))
                        .testTag("stat_cost_drivers_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔥 TOP COST DRIVERS & ANALYTICS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BentoRose,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "${uiState.topCostDriverPercentage.toInt()}% of spending",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BentoRose
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Biggest Category", style = MaterialTheme.typography.labelSmall, color = BentoRose)
                                    Text(
                                        text = uiState.topCostDriverCategory,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "$symbol${uiState.topCostDriverAmount.toInt()}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BentoRose
                                    )
                                }
                            }

                            if (uiState.highestSingleTransactionAmount > 0) {
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = BentoAmberLight)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("Single Biggest Expense", style = MaterialTheme.typography.labelSmall, color = BentoAmber)
                                        Text(
                                            text = uiState.highestSingleTransactionDesc,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "$symbol${uiState.highestSingleTransactionAmount.toInt()}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = BentoAmber
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = uiState.costDriverSuggestion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // RECURRING INFLOW & INCOME STREAMS CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoEmeraldLight, RoundedCornerShape(24.dp))
                    .testTag("stat_recurring_income_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💵 MONTHLY INFLOW & RECURRING STREAMS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoEmerald,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Est. Inflow: $symbol${uiState.totalProjectedMonthlyInflow.toInt()}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = BentoEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.recurringIncomeStreams.isEmpty()) {
                        Text(
                            text = "No recurring income logged yet. When adding income in Ledger, toggle 'Recurring Income' (Daily, Weekly, or Monthly) to calculate automatic monthly inflow projections.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Active Income Streams:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            uiState.recurringIncomeStreams.forEach { stream ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF0FDF4))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = stream.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(BentoEmeraldLight)
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = stream.frequency,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = BentoEmerald
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Base: $symbol${stream.baseAmount.toInt()} / ${stream.frequency.lowercase()}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "+$symbol${stream.projectedMonthlyInflow.toInt()}/mo",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = BentoEmerald
                                        )
                                        Text(
                                            text = "Logged: $symbol${stream.totalLogged.toInt()}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bento Grid Block 3: Loans & Debt Summary Dark Bento Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToLoans() }
                    .testTag("stat_loans_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoDarkCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = BentoIndigoContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "LOANS & DEBT SUMMARY",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BentoIndigoContainer,
                                letterSpacing = 0.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Text(
                            text = "${uiState.daysLogged} days logged",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "I Owe (Borrowed)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "$symbol${totalIOwe.toInt()}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BentoRose,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Owed To Me (Lent)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "$symbol${totalOwedToMe.toInt()}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BentoEmerald,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Financial Tasks & Reminders Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoCardBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ListAlt,
                                contentDescription = null,
                                tint = BentoIndigoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "FINANCIAL TASKS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.5.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { showAddTaskDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BentoIndigoPrimary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = BentoIndigoPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Task", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BentoIndigoPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (tasks.isEmpty()) {
                        Text(
                            text = "No financial tasks yet. Tap 'Add Task' to list upcoming bills, savings deposits, or loan repayments.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        tasks.take(5).forEach { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = { viewModel.toggleTaskCompleted(task) },
                                    colors = CheckboxDefaults.colors(checkedColor = BentoIndigoPrimary)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (task.dueDate.isNotBlank() || task.category.isNotBlank()) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (task.category.isNotBlank()) {
                                                Text(
                                                    text = task.category,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = BentoIndigoPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            if (task.dueDate.isNotBlank()) {
                                                Text(
                                                    text = "• ${task.dueDate}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { viewModel.deleteTask(task.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete task",
                                        tint = CoralExpense.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bento Dynamic Insight Banner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToLedger() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BentoIndigoLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoIndigoContainer)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BentoIndigoContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Insight",
                            tint = BentoIndigoPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BUDGET INSIGHT",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BentoIndigoPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = uiState.insightMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Doughnut Chart Bento Box
            DoughnutChart(
                spentAmount = uiState.totalSpentTillToday,
                remainingAmount = (uiState.initialAmount - uiState.totalSpentTillToday).coerceAtLeast(0.0),
                currencySymbol = symbol,
                onChartClick = onNavigateToLedger
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Bar Charts
            CategoryBarChart(
                title = "Spent vs Categories",
                items = uiState.categorySpendList,
                currencySymbol = symbol,
                onCategoryClick = { _ -> onNavigateToLedger() }
            )

            Spacer(modifier = Modifier.height(14.dp))

            CategoryBarChart(
                title = "Sub-Category Breakdown",
                items = uiState.subCategorySpendList,
                currencySymbol = symbol,
                onCategoryClick = { _ -> onNavigateToLedger() }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onAddExpenseClick,
            containerColor = BentoIndigoPrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_add_transaction")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Transaction")
        }
    }

    // Profile Setup Dialog
    if (showProfileSetupDialog) {
        var nameInput by remember { mutableStateOf(rawProfileName) }
        var cashInput by remember { mutableStateOf(uiState.initialAmount.toInt().toString()) }
        var targetSavingsInput by remember { mutableStateOf((userSettings?.targetSavings ?: 0.0).toInt().toString()) }
        var targetBudgetInput by remember { mutableStateOf((userSettings?.targetBudget ?: 0.0).toInt().toString()) }
        var dayInput by remember { mutableStateOf((userSettings?.salaryDay ?: 1).toString()) }
        var currInput by remember { mutableStateOf(symbol) }
        var frequencyInput by remember { mutableStateOf(userSettings?.incomeFrequency ?: "Monthly") }

        AlertDialog(
            onDismissRequest = { showProfileSetupDialog = false },
            title = { Text("TakaKoi Budget & Targets Setup", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Your Name") },
                        placeholder = { Text("e.g. Alex") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = cashInput,
                        onValueChange = { cashInput = it },
                        label = { Text("Starting Wallet Cash / Monthly Income") },
                        placeholder = { Text("e.g. 25000") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = targetBudgetInput,
                        onValueChange = { targetBudgetInput = it },
                        label = { Text("Target Spend Budget Limit for Month") },
                        placeholder = { Text("e.g. 18000") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = targetSavingsInput,
                        onValueChange = { targetSavingsInput = it },
                        label = { Text("Target Monthly Savings Goal") },
                        placeholder = { Text("e.g. 5000") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = dayInput,
                        onValueChange = { dayInput = it },
                        label = { Text("Payday / Salary Day of Month (1-31)") },
                        placeholder = { Text("1") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Primary Income Recurrence:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Monthly", "Weekly", "Daily", "Business").forEach { freq ->
                            FilterChip(
                                selected = frequencyInput == freq,
                                onClick = { frequencyInput = freq },
                                label = { Text(freq, fontSize = 11.sp) }
                            )
                        }
                    }

                    Text("Preferred Currency Symbol:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("৳", "$", "₹", "€", "£").forEach { c ->
                            FilterChip(
                                selected = currInput == c,
                                onClick = { currInput = c },
                                label = { Text(c, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cash = cashInput.toDoubleOrNull() ?: 0.0
                        val targetSavings = targetSavingsInput.toDoubleOrNull() ?: 0.0
                        val targetBudget = targetBudgetInput.toDoubleOrNull() ?: 0.0
                        val day = (dayInput.toIntOrNull() ?: 1).coerceIn(1, 31)
                        viewModel.updateUserSettings(
                            userName = nameInput,
                            initialCash = cash,
                            salaryDay = day,
                            currencySymbol = currInput,
                            targetSavings = targetSavings,
                            targetBudget = targetBudget,
                            incomeFrequency = frequencyInput
                        )
                        showProfileSetupDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BentoIndigoPrimary)
                ) {
                    Text("Save Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileSetupDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Task Dialog
    if (showAddTaskDialog) {
        var taskTitle by remember { mutableStateOf("") }
        var taskCategory by remember { mutableStateOf("Bills") }
        var taskDueDate by remember { mutableStateOf("") }
        var taskPriority by remember { mutableStateOf("Medium") }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text("Add Financial Task", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Description") },
                        placeholder = { Text("e.g. Pay Internet Bill") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = taskDueDate,
                        onValueChange = { taskDueDate = it },
                        label = { Text("Due Date / Timeframe") },
                        placeholder = { Text("e.g. In 3 days / 15th of Month") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Category:", style = MaterialTheme.typography.bodySmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Bills", "Savings", "Loans", "General").forEach { cat ->
                            FilterChip(
                                selected = taskCategory == cat,
                                onClick = { taskCategory = cat },
                                label = { Text(cat, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            viewModel.addTask(
                                title = taskTitle,
                                category = taskCategory,
                                dueDate = taskDueDate,
                                priority = taskPriority
                            )
                        }
                        showAddTaskDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BentoIndigoPrimary)
                ) {
                    Text("Add Task")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
