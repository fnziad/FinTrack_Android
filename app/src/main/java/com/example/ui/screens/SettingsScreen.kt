package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BentoCardBorder
import com.example.ui.theme.BentoIndigoPrimary
import com.example.ui.theme.CoralExpense
import com.example.ui.theme.EmeraldGreen
import com.example.ui.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val userSettings by viewModel.userSettings.collectAsState()

    var userName by remember(userSettings?.userName) { mutableStateOf(userSettings?.userName.orEmpty()) }
    var initialCashText by remember(userSettings?.initialCash) { mutableStateOf((userSettings?.initialCash ?: 0.0).toInt().toString()) }
    var salaryDayText by remember(userSettings?.salaryDay) { mutableStateOf((userSettings?.salaryDay ?: 1).toString()) }
    var selectedCurrency by remember(userSettings?.currencySymbol) { mutableStateOf(userSettings?.currencySymbol ?: "৳") }
    val isDarkMode = userSettings?.isDarkMode ?: false

    var showResetDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Settings & Customization",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Configure your profile, theme mode, currency & starting budget",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Appearance & Dark Mode Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BentoCardBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = BentoIndigoPrimary,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = if (isDarkMode) "Dark Theme" else "Light Theme",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Toggle display theme mode",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BentoIndigoPrimary
                    ),
                    modifier = Modifier.testTag("switch_dark_mode")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "User Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("User / Profile Name") },
                    placeholder = { Text("e.g. Alex") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Currency Preferences
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Currency Preferences",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("৳", "$", "₹", "€", "£").forEach { curr ->
                        FilterChip(
                            selected = selectedCurrency == curr,
                            onClick = { selectedCurrency = curr },
                            label = { Text(curr, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Financial Cycle Parameters
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Budget & Payday",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = initialCashText,
                    onValueChange = { initialCashText = it },
                    label = { Text("Starting Budget / Income ($selectedCurrency)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = salaryDayText,
                    onValueChange = { salaryDayText = it },
                    label = { Text("Salary / Payment Day of Month (1-31)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val initCash = initialCashText.toDoubleOrNull() ?: 0.0
                        val salDay = (salaryDayText.toIntOrNull() ?: 1).coerceIn(1, 31)
                        viewModel.updateUserSettings(userName, initCash, salDay, selectedCurrency)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BentoIndigoPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Settings", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Data Management & Demo Actions
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BentoCardBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = null,
                        tint = BentoIndigoPrimary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Data Management",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showClearDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_clear_data"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = CoralExpense,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Clear All Data", fontWeight = FontWeight.Bold, color = CoralExpense)
                    }

                    Button(
                        onClick = { showResetDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_reset_sample"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = BentoIndigoPrimary,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Load Sample Data", fontWeight = FontWeight.Bold, color = BentoIndigoPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "FinTrack v1.0 • All user profile, transaction & task data stored locally in offline Room database.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Data?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently delete all logged transactions, savings goals, loan records, and tasks from your device database.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllData()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralExpense)
                ) {
                    Text("Clear Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Load Sample Demo Data?", fontWeight = FontWeight.Bold) },
            text = { Text("This will populate sample transactions, savings goals, loan entries, and financial tasks so you can preview the app's features.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.loadDemoData()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BentoIndigoPrimary)
                ) {
                    Text("Load Sample Data")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }
}
