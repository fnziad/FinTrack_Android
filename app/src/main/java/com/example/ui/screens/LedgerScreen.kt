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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionEntity
import com.example.ui.theme.CoralExpense
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MintIncome
import com.example.ui.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerScreen(
    viewModel: ExpenseViewModel,
    showAddDialogInitially: Boolean = false,
    onDialogDismissed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.allTransactions.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val symbol = userSettings?.currencySymbol ?: "৳"

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "EXPENSE", "INCOME"

    var showAddModal by remember { mutableStateOf(showAddDialogInitially) }

    // Filtered items
    val filteredList = transactions.filter { tx ->
        val matchesSearch = tx.description.contains(searchQuery, ignoreCase = true) ||
                tx.category.contains(searchQuery, ignoreCase = true) ||
                tx.subCategory.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "EXPENSE" -> tx.type == "EXPENSE"
            "INCOME" -> tx.type == "INCOME"
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Transaction Ledger",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Detailed record of income, mess bills, transport & expenses",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search description, subcategory...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ledger_search_field"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == "ALL",
                        onClick = { selectedFilter = "ALL" },
                        label = { Text("All Transactions") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "EXPENSE",
                        onClick = { selectedFilter = "EXPENSE" },
                        label = { Text("Expenses Only") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "INCOME",
                        onClick = { selectedFilter = "INCOME" },
                        label = { Text("Income / Salary") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Transaction Table / List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "No transactions found",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tap '+' to add your first expense or income!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        TransactionItemCard(
                            transaction = item,
                            currencySymbol = symbol,
                            onDelete = { viewModel.deleteTransaction(item.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddModal = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .testTag("ledger_fab_add")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Item")
        }
    }

    if (showAddModal) {
        AddTransactionDialog(
            currencySymbol = symbol,
            onDismiss = {
                showAddModal = false
                onDialogDismissed()
            },
            onConfirm = { type, amount, category, subCategory, description, isRecurring, frequency ->
                viewModel.addTransaction(
                    type = type,
                    amount = amount,
                    category = category,
                    subCategory = subCategory,
                    description = description,
                    isRecurring = isRecurring,
                    recurringFrequency = frequency
                )
                showAddModal = false
                onDialogDismissed()
            }
        )
    }
}

@Composable
fun TransactionItemCard(
    transaction: TransactionEntity,
    currencySymbol: String,
    onDelete: () -> Unit
) {
    val isExpense = transaction.type == "EXPENSE"
    val dateStr = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(transaction.dateEpochMillis))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_item_${transaction.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isExpense) Color(0xFFFEE2E2) else Color(0xFFD1FAE5))
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = if (isExpense) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = if (isExpense) CoralExpense else MintIncome
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = transaction.description.ifEmpty { transaction.subCategory },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${transaction.category} • ${transaction.subCategory}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        if (transaction.isRecurring) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "🔁 ${transaction.recurringFrequency}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${if (isExpense) "-" else "+"}$currencySymbol${transaction.amount.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isExpense) CoralExpense else MintIncome,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 4.dp)
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (type: String, amount: Double, category: String, subCategory: String, description: String, isRecurring: Boolean, frequency: String) -> Unit
) {
    var type by remember { mutableStateOf("EXPENSE") }
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food & Mess") }
    var subCategory by remember { mutableStateOf("Rickshaw Fare") }
    var description by remember { mutableStateOf("") }
    var isRecurring by remember { mutableStateOf(false) }
    var recurringFrequency by remember { mutableStateOf("Monthly") }

    val categories = listOf(
        "Food & Mess",
        "Transport",
        "Education & Tuition",
        "Utilities & Mobile",
        "Shopping & Personal",
        "Salary & Income",
        "Pocket Money & Stipend",
        "Business & Side Hustle",
        "Loans & Debt",
        "Miscellaneous"
    )

    val presetSubCategories = mapOf(
        "Food & Mess" to listOf("Rickshaw Fare", "Bus / Metro", "Mess Meal & Rent", "Tea Stall & Snacks", "Restaurant / Dining"),
        "Transport" to listOf("Rickshaw Fare", "Bus Fare", "Metro Rail Pass", "CNG / Pathao", "Train / Launch Ticket"),
        "Education & Tuition" to listOf("Tuition Fee", "University Semester Fee", "Books & Printouts", "Skill Course"),
        "Utilities & Mobile" to listOf("Mobile Recharge", "Bkash/Nagad Cashout Fee", "Wifi Bill", "Electricity/Gas Share"),
        "Shopping & Personal" to listOf("Clothes & Shoes", "Groceries", "Medicine", "Barber/Salon"),
        "Salary & Income" to listOf("Monthly Salary", "Tuition Income", "Freelance", "Pocket Money"),
        "Pocket Money & Stipend" to listOf("Daily Allowance", "Weekly Pocket Money", "Parents Stipend"),
        "Business & Side Hustle" to listOf("Daily Shop Revenue", "Online Store Sales", "Client Project"),
        "Loans & Debt" to listOf("Borrowed Repayment", "Lent Money", "Mess Loan"),
        "Miscellaneous" to listOf("General", "Gift", "Entertainment")
    )

    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log New Transaction",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Expense / Income toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { type = "EXPENSE" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "EXPENSE") CoralExpense else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (type == "EXPENSE") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Expense")
                    }
                    Button(
                        onClick = { type = "INCOME" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "INCOME") MintIncome else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (type == "INCOME") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Income")
                    }
                }

                // Amount
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount ($currencySymbol)") },
                    placeholder = { Text("e.g. 180") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_amount_input"),
                    singleLine = true
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    subCategory = presetSubCategories[cat]?.firstOrNull() ?: "General"
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // SubCategory Text
                OutlinedTextField(
                    value = subCategory,
                    onValueChange = { subCategory = it },
                    label = { Text("Sub-category") },
                    placeholder = { Text("e.g. Rickshaw Fare, Tea Stall") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Note") },
                    placeholder = { Text("e.g. Rickshaw to Farmgate metro station") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Recurring Income / Expense Toggle
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRecurring = !isRecurring },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (type == "INCOME") "Recurring Income? (e.g. Daily sales, Weekly pocket money)" else "Recurring Expense?",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        androidx.compose.material3.Switch(
                            checked = isRecurring,
                            onCheckedChange = { isRecurring = it }
                        )
                    }

                    if (isRecurring) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Recurring Frequency:", style = MaterialTheme.typography.labelSmall)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Daily", "Weekly", "Monthly").forEach { freq ->
                                FilterChip(
                                    selected = recurringFrequency == freq,
                                    onClick = { recurringFrequency = freq },
                                    label = { Text(freq, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (amount > 0) {
                        onConfirm(type, amount, category, subCategory, description, isRecurring, if (isRecurring) recurringFrequency else "One-time")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Text("Save Transaction")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
