package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import com.example.data.model.LoanEntity
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.CoralExpense
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MintIncome
import com.example.ui.viewmodel.ExpenseViewModel

@Composable
fun LoansScreen(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val loans by viewModel.allLoans.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val symbol = userSettings?.currencySymbol ?: "৳"

    var selectedTypeFilter by remember { mutableStateOf("ALL") } // "ALL", "SHORT_TERM", "LONG_TERM"
    var showAddLoanModal by remember { mutableStateOf(false) }
    var selectedRepayLoan by remember { mutableStateOf<LoanEntity?>(null) }

    val filteredLoans = loans.filter { loan ->
        when (selectedTypeFilter) {
            "SHORT_TERM" -> loan.loanType == "SHORT_TERM"
            "LONG_TERM" -> loan.loanType == "LONG_TERM"
            else -> true
        }
    }

    val totalIOwe = loans.filter { it.direction == "I_OWE" && !it.isSettled }.sumOf { it.amount - it.paidAmount }
    val totalOwedToMe = loans.filter { it.direction == "OWED_TO_ME" && !it.isSettled }.sumOf { it.amount - it.paidAmount }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Loans & Debts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Track short-term mess/rickshaw dues & long-term laptop EMIs",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Debt Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CoralExpense.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "I Owe (Borrowed)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CoralExpense,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Text(
                            text = "$symbol${totalIOwe.toInt()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = CoralExpense,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldGreen.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Owed To Me (Lent)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Text(
                            text = "$symbol${totalOwedToMe.toInt()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldGreen,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips (Short Term vs Long Term)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedTypeFilter == "ALL",
                        onClick = { selectedTypeFilter = "ALL" },
                        label = { Text("All Dues") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedTypeFilter == "SHORT_TERM",
                        onClick = { selectedTypeFilter = "SHORT_TERM" },
                        label = { Text("Short Term Loans") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedTypeFilter == "LONG_TERM",
                        onClick = { selectedTypeFilter = "LONG_TERM" },
                        label = { Text("Long Term Loans") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredLoans.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "No loan records found",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tap '+' to record borrowed mess rent or laptop EMI!",
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
                    items(filteredLoans, key = { it.id }) { loan ->
                        LoanCardItem(
                            loan = loan,
                            currencySymbol = symbol,
                            onRepay = { selectedRepayLoan = loan },
                            onDelete = { viewModel.deleteLoan(loan.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddLoanModal = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .testTag("loans_fab_add")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Loan")
        }
    }

    if (showAddLoanModal) {
        AddLoanDialog(
            currencySymbol = symbol,
            onDismiss = { showAddLoanModal = false },
            onConfirm = { title, person, amount, loanType, direction, note ->
                viewModel.addLoan(title, person, amount, loanType, direction, note)
                showAddLoanModal = false
            }
        )
    }

    if (selectedRepayLoan != null) {
        RepayLoanDialog(
            loan = selectedRepayLoan!!,
            currencySymbol = symbol,
            onDismiss = { selectedRepayLoan = null },
            onConfirm = { repay ->
                viewModel.repayLoan(selectedRepayLoan!!, repay)
                selectedRepayLoan = null
            }
        )
    }
}

@Composable
fun LoanCardItem(
    loan: LoanEntity,
    currencySymbol: String,
    onRepay: () -> Unit,
    onDelete: () -> Unit
) {
    val isBorrow = loan.direction == "I_OWE"
    val remaining = (loan.amount - loan.paidAmount).coerceAtLeast(0.0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("loan_card_${loan.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isBorrow) CoralExpense.copy(alpha = 0.15f) else EmeraldGreen.copy(alpha = 0.15f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (loan.loanType == "SHORT_TERM") "Short" else "Long",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isBorrow) CoralExpense else EmeraldGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            text = loan.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${if (isBorrow) "To: " else "From: "}${loan.personName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Due: $currencySymbol${loan.amount.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Remaining: $currencySymbol${remaining.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (loan.isSettled) EmeraldGreen else if (isBorrow) CoralExpense else MintIncome
                    )
                }

                if (loan.isSettled) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Settled",
                            tint = EmeraldGreen,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = "SETTLED",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    }
                } else {
                    Button(
                        onClick = onRepay,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Record Payment",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddLoanDialog(
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, personName: String, amount: Double, loanType: String, direction: String, note: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var personName by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var loanType by remember { mutableStateOf("SHORT_TERM") } // SHORT_TERM / LONG_TERM
    var direction by remember { mutableStateOf("I_OWE") } // I_OWE / OWED_TO_ME
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Loan / Debt Record", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Direction Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { direction = "I_OWE" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (direction == "I_OWE") CoralExpense else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (direction == "I_OWE") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("I Owe (Borrow)")
                    }
                    Button(
                        onClick = { direction = "OWED_TO_ME" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (direction == "OWED_TO_ME") EmeraldGreen else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (direction == "OWED_TO_ME") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Owed to Me")
                    }
                }

                // Type Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = loanType == "SHORT_TERM",
                        onClick = { loanType = "SHORT_TERM" },
                        label = { Text("Short Term (Mess/Rent)") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = loanType == "LONG_TERM",
                        onClick = { loanType = "LONG_TERM" },
                        label = { Text("Long Term (EMI/Laptop)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    placeholder = { Text("e.g. Mess rent share, Laptop EMI") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = personName,
                    onValueChange = { personName = it },
                    label = { Text("Person / Store Name") },
                    placeholder = { Text("e.g. Mess Manager Tanvir, Roommate") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount ($currencySymbol)") },
                    placeholder = { Text("e.g. 1200") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    placeholder = { Text("e.g. To be cleared on salary day") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (title.isNotEmpty() && amount > 0) {
                        onConfirm(title, personName, amount, loanType, direction, note)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Text("Save Record")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun RepayLoanDialog(
    loan: LoanEntity,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (repayAmount: Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val remaining = loan.amount - loan.paidAmount

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Payment for ${loan.title}", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    text = "Remaining balance: $currencySymbol${remaining.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Repayment Amount ($currencySymbol)") },
                    placeholder = { Text("e.g. ${remaining.toInt()}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val repay = amountText.toDoubleOrNull() ?: 0.0
                    if (repay > 0) onConfirm(repay)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Text("Record Payment")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
