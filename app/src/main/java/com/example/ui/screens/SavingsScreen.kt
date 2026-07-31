package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.SavingsGoalEntity
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MintIncome
import com.example.ui.viewmodel.ExpenseViewModel

@Composable
fun SavingsScreen(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.allSavingsGoals.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val symbol = userSettings?.currencySymbol ?: "৳"

    var showAddGoalDialog by remember { mutableStateOf(false) }
    var selectedDepositGoal by remember { mutableStateOf<SavingsGoalEntity?>(null) }

    val totalSaved = goals.sumOf { it.currentAmount }
    val totalTarget = goals.sumOf { it.targetAmount }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Savings Wallets",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Emergency fund, Eid savings & DPS monthly target trackers",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Overall Savings Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            text = "Total Accumulated Savings",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$symbol${totalSaved.toInt()} / $symbol${totalTarget.toInt()}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(EmeraldGreen)
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "No savings wallets yet",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tap '+' to set up your Emergency Fund or DPS target!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(goals, key = { it.id }) { goal ->
                        SavingsGoalCard(
                            goal = goal,
                            currencySymbol = symbol,
                            onAddDeposit = { selectedDepositGoal = goal },
                            onDelete = { viewModel.deleteSavingsGoal(goal.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddGoalDialog = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .testTag("savings_fab_add")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create Wallet")
        }
    }

    if (showAddGoalDialog) {
        AddSavingsGoalDialog(
            currencySymbol = symbol,
            onDismiss = { showAddGoalDialog = false },
            onConfirm = { title, target, initial, category, note ->
                viewModel.addSavingsGoal(title, target, initial, category, note)
                showAddGoalDialog = false
            }
        )
    }

    if (selectedDepositGoal != null) {
        DepositDialog(
            goal = selectedDepositGoal!!,
            currencySymbol = symbol,
            onDismiss = { selectedDepositGoal = null },
            onConfirm = { added ->
                viewModel.updateSavingsAmount(selectedDepositGoal!!, added)
                selectedDepositGoal = null
            }
        )
    }
}

@Composable
fun SavingsGoalCard(
    goal: SavingsGoalEntity,
    currencySymbol: String,
    onAddDeposit: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = (goal.currentAmount / goal.targetAmount.coerceAtLeast(1.0)).toFloat().coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "savingsProgress")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("savings_card_${goal.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    if (goal.note.isNotEmpty()) {
                        Text(
                            text = goal.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$currencySymbol${goal.currentAmount.toInt()} / $currencySymbol${goal.targetAmount.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = EmeraldGreen
                )
                Text(
                    text = "${(progress * 100).toInt()}% completed",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(RoundedCornerShape(5.dp))
                        .background(EmeraldGreen)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddDeposit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "Deposit / Add Funds",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun AddSavingsGoalDialog(
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, target: Double, initial: Double, category: String, note: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }
    var initialText by remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Savings Wallet", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Wallet / Goal Title") },
                    placeholder = { Text("e.g. Emergency Fund, Laptop EMI") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    label = { Text("Target Amount ($currencySymbol)") },
                    placeholder = { Text("e.g. 20000") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = initialText,
                    onValueChange = { initialText = it },
                    label = { Text("Starting Deposit ($currencySymbol)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note / Description") },
                    placeholder = { Text("e.g. Monthly DPS deposit target") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val target = targetText.toDoubleOrNull() ?: 0.0
                    val initial = initialText.toDoubleOrNull() ?: 0.0
                    if (title.isNotEmpty() && target > 0) {
                        onConfirm(title, target, initial, "General", note)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Text("Create Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun DepositDialog(
    goal: SavingsGoalEntity,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (addedAmount: Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Deposit to ${goal.title}", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    text = "Current balance: $currencySymbol${goal.currentAmount.toInt()} / $currencySymbol${goal.targetAmount.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Deposit Amount ($currencySymbol)") },
                    placeholder = { Text("e.g. 1000") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val added = amountText.toDoubleOrNull() ?: 0.0
                    if (added > 0) onConfirm(added)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Text("Add Deposit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
