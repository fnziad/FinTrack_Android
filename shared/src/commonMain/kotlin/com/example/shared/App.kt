package com.example.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shared.ui.screens.DashboardScreen
import com.example.shared.ui.screens.LedgerScreen
import com.example.shared.ui.screens.LoansScreen
import com.example.shared.ui.screens.SavingsScreen
import com.example.shared.ui.screens.SettingsScreen
import com.example.shared.ui.theme.TakaTrackTheme
import com.example.shared.ui.viewmodel.ExpenseViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Ledger : Screen("ledger", "Ledger", Icons.Default.Receipt)
    object Savings : Screen("savings", "Savings", Icons.Default.Savings)
    object Loans : Screen("loans", "Loans", Icons.Default.AccountBalance)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun App(viewModel: ExpenseViewModel = viewModel { ExpenseViewModel() }) {
    val userSettings by viewModel.userSettings.collectAsState()
    val isDark = userSettings?.isDarkMode ?: isSystemInDarkTheme()
    val themePreset = userSettings?.colorTheme ?: "INDIGO"

    TakaTrackTheme(darkTheme = isDark, colorTheme = themePreset) {
        MainAppStructure(viewModel = viewModel)
    }
}

@Composable
fun MainAppStructure(viewModel: ExpenseViewModel) {
    var currentRoute by remember { mutableStateOf(Screen.Dashboard.route) }
    var showAddDialogFromDashboard by remember { mutableStateOf(false) }

    val items = listOf(
        Screen.Dashboard,
        Screen.Ledger,
        Screen.Savings,
        Screen.Loans,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                items.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentRoute = screen.route },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentRoute) {
                Screen.Dashboard.route -> {
                    DashboardScreen(
                        viewModel = viewModel,
                        onAddExpenseClick = {
                            showAddDialogFromDashboard = true
                            currentRoute = Screen.Ledger.route
                        },
                        onNavigateToLedger = { currentRoute = Screen.Ledger.route },
                        onNavigateToLoans = { currentRoute = Screen.Loans.route },
                        onNavigateToSavings = { currentRoute = Screen.Savings.route },
                        onNavigateToSettings = { currentRoute = Screen.Settings.route }
                    )
                }
                Screen.Ledger.route -> {
                    LedgerScreen(
                        viewModel = viewModel,
                        showAddDialogInitially = showAddDialogFromDashboard,
                        onDialogDismissed = { showAddDialogFromDashboard = false }
                    )
                }
                Screen.Savings.route -> {
                    SavingsScreen(viewModel = viewModel)
                }
                Screen.Loans.route -> {
                    LoansScreen(viewModel = viewModel)
                }
                Screen.Settings.route -> {
                    SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
