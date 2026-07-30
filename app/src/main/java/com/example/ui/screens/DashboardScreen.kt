package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CategoryBarChart
import com.example.ui.components.DoughnutChart
import com.example.ui.components.StatCard
import com.example.ui.theme.AmberTertiary
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
import com.example.ui.viewmodel.ExpenseViewModel

@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel,
    onAddExpenseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.dashboardUiState.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val loans by viewModel.allLoans.collectAsState()

    val profileName = userSettings?.userName ?: "Sajid Ahmed"
    val profileType = userSettings?.profileType ?: "FRESHER"
    val symbol = uiState.currencySymbol

    val totalIOwe = loans.filter { it.direction == "I_OWE" && !it.isSettled }.sumOf { it.amount - it.paidAmount }
    val totalOwedToMe = loans.filter { it.direction == "OWED_TO_ME" && !it.isSettled }.sumOf { it.amount - it.paidAmount }

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
                Column {
                    Text(
                        text = "WELCOME, ${profileName.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "FinTrack",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BentoIndigoLight)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = profileType,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BentoIndigoPrimary
                        )
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

            // Bento Grid Block 1: Salary Countdown (Hero Card) + Side Budget Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Hero Salary Countdown Bento Box (2x2 proportion)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .testTag("stat_days_until_salary"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoIndigoPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = BentoIndigoContainer,
                            modifier = Modifier.size(28.dp)
                        )

                        Column {
                            Text(
                                text = "${uiState.daysUntilSalary}",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 38.sp),
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic,
                                color = Color.White
                            )
                            Text(
                                text = "DAYS UNTIL SALARY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Column of 2 Mini Bento Cards (Initial Budget & Wallet Cash)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Initial Budget Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(1.dp, BentoCardBorder, RoundedCornerShape(20.dp))
                            .testTag("stat_initial_amount"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
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
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "BUDGET",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$symbol${uiState.initialAmount.toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Wallet Cash Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(1.dp, BentoCardBorder, RoundedCornerShape(20.dp))
                            .testTag("stat_wallet_cash"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
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
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "WALLET CASH",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$symbol${uiState.walletCash.toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
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
                        .testTag("stat_spent_today"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Month so far",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$symbol${uiState.totalSpentTillToday.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoRose
                        )
                    }
                }

                // Daily Avg & Target Bento Box
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, BentoCardBorder, RoundedCornerShape(24.dp))
                        .testTag("stat_daily_avg"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "DAILY AVG SPENT",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$symbol${uiState.dailyAvgSpent.toInt()}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
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
                            color = BentoEmerald
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bento Grid Block 3: Loans & Debt Summary Dark Bento Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stat_total_spent"),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                letterSpacing = 0.5.sp
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
                        Column {
                            Text(
                                text = "I Owe (Borrowed)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "$symbol${totalIOwe.toInt()}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BentoRose
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Owed To Me (Lent)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "$symbol${totalOwedToMe.toInt()}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BentoEmerald
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bento Dynamic Insight Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                    Column {
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
                remainingAmount = (uiState.initialAmount - uiState.totalSpentTillToday),
                currencySymbol = symbol
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Bar Charts
            CategoryBarChart(
                title = "Spent vs Categories",
                items = uiState.categorySpendList,
                currencySymbol = symbol
            )

            Spacer(modifier = Modifier.height(14.dp))

            CategoryBarChart(
                title = "Sub-Category Breakdown",
                items = uiState.subCategorySpendList,
                currencySymbol = symbol
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
}
