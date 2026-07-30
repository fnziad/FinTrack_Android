package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LedgerScreen
import com.example.ui.screens.LoansScreen
import com.example.ui.screens.SavingsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BentoIndigoContainer
import com.example.ui.theme.BentoIndigoPrimary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TakaTrackTheme
import com.example.ui.viewmodel.ExpenseViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Ledger : Screen("ledger", "Ledger", Icons.Default.Receipt)
    object Savings : Screen("savings", "Savings", Icons.Default.Savings)
    object Loans : Screen("loans", "Loans & Debt", Icons.Default.AccountBalance)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    private val viewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userSettings by viewModel.userSettings.collectAsState()
            val isDark = userSettings?.isDarkMode ?: androidx.compose.foundation.isSystemInDarkTheme()

            TakaTrackTheme(darkTheme = isDark) {
                MainAppStructure(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppStructure(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

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
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
            ) {
                items.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
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
                            selectedIconColor = BentoIndigoPrimary,
                            selectedTextColor = BentoIndigoPrimary,
                            indicatorColor = BentoIndigoContainer
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onAddExpenseClick = {
                        showAddDialogFromDashboard = true
                        navController.navigate(Screen.Ledger.route)
                    }
                )
            }

            composable(Screen.Ledger.route) {
                LedgerScreen(
                    viewModel = viewModel,
                    showAddDialogInitially = showAddDialogFromDashboard,
                    onDialogDismissed = { showAddDialogFromDashboard = false }
                )
            }

            composable(Screen.Savings.route) {
                SavingsScreen(viewModel = viewModel)
            }

            composable(Screen.Loans.route) {
                LoansScreen(viewModel = viewModel)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
