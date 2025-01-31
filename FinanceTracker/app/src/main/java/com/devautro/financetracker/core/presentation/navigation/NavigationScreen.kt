package com.devautro.financetracker.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devautro.financetracker.R
import com.devautro.financetracker.core.presentation.TabBarItem
import com.devautro.financetracker.core.presentation.components.BottomNavigationBar
import com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.add_money_source.AddMoneySource
import com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.edit_money_source.EditMoneySource
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.MoneySources
import com.devautro.financetracker.feature_payment.presentation.add_payment.AddPaymentBottomSheet
import com.devautro.financetracker.feature_payment.presentation.add_payment.AddPaymentViewModel
import com.devautro.financetracker.feature_payment.presentation.home_screen.HomeScreen
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.expenses.ExpensesList
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.incomes.IncomesList
import com.devautro.financetracker.feature_settings.presentation.SettingsMain
import com.devautro.financetracker.feature_settings.presentation.SettingsViewModel
import com.devautro.financetracker.feature_statistics.presentation.StatisticsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    settingsViewModel: SettingsViewModel,
    currencySign: String = ""
) {
    val barItems = listOf(
        TabBarItem(
            name = stringResource(id = R.string.payments_tab),
            route = Home,
            selectedIcon = Icons.Filled.Payments,
            unselectedIcon = Icons.Outlined.Payments
        ),
        TabBarItem(
            name = stringResource(id = R.string.charts_tab),
            route = Charts,
            selectedIcon = Icons.Filled.PieChart,
            unselectedIcon = Icons.Outlined.PieChartOutline
        ),
        TabBarItem(
            name = stringResource(id = R.string.accounts_tab),
            route = Accounts,
            selectedIcon = Icons.Filled.AccountBalanceWallet,
            unselectedIcon = Icons.Outlined.AccountBalanceWallet
        ),
        TabBarItem(
            name = stringResource(id = R.string.settings_tab),
            route = Settings,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )

    val navController = rememberNavController()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    showBottomBar = when {
        currentDestination?.hasRoute(Expenses::class) == true -> false
        currentDestination?.hasRoute(Incomes::class) == true -> false
        currentDestination?.hasRoute(AddAccount::class) == true -> false
        currentDestination?.hasRoute(EditAccount::class) == true -> false

        else -> true
    }


    val showBottomSheet =
        rememberSaveable { mutableStateOf(false) } // to survive configuration changes
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val addPaymentViewModel = hiltViewModel<AddPaymentViewModel>()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = showBottomBar) {
                BottomNavigationBar(
                    barItems = barItems,
                    navController = navController,
                    currentDestination = currentDestination
                )
            }

        },
        floatingActionButton = {
            if (showBottomBar && (currentDestination?.hasRoute(Settings::class) == false)) {
                FloatingActionButton(
                    onClick = {

                        showBottomSheet.value = true
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .offset(y = 32.dp)
                        .zIndex(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.fab_description),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { bottomNavPadding ->

        NavHost(
            navController = navController,
            startDestination = Home,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable<Home> {
                HomeScreen(
                    bottomPadding = bottomNavPadding,
                    navigateToIncomes = { navController.navigate(Incomes) },
                    navigateToExpenses = { navController.navigate(Expenses) },
                    currencySign = currencySign
                )

                if (showBottomSheet.value) {
                    AddPaymentBottomSheet(
                        viewModel = addPaymentViewModel,
                        sheetState = sheetState,
                        navigateBack = {
                            showBottomSheet.value = false
                        }
                    )
                }
            }

            composable<Incomes>(
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                IncomesList(
                    navigateBack = { navController.navigateUp() },
                    navBarPadding = bottomNavPadding,
                    currencySign = currencySign
                )
            }

            composable<Expenses>(
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                ExpensesList(
                    navigateBack = { navController.navigateUp() },
                    navBarPadding = bottomNavPadding,
                    currencySign = currencySign
                )
            }

            composable<Charts> {


                StatisticsScreen(bottomPaddingValues = bottomNavPadding, currency = currencySign)

                if (showBottomSheet.value) {
                    AddPaymentBottomSheet(
                        viewModel = addPaymentViewModel,
                        sheetState = sheetState,
                        navigateBack = {
                            showBottomSheet.value = false
                        }
                    )
                }
            }

            composable<Accounts> {
                MoneySources(
                    bottomNavPadding = bottomNavPadding,
                    navigateToAddScreen = {
                        navController.navigate(AddAccount)
                    },
                    navigateToEditScreen = { id, color ->
                        navController.navigate(
                            EditAccount(
                                id = id,
                                paleColor = color
                            )
                        )
                    },
                    currencySign = currencySign
                )

                if (showBottomSheet.value) {
                    AddPaymentBottomSheet(
                        viewModel = addPaymentViewModel,
                        sheetState = sheetState,
                        navigateBack = {
                            showBottomSheet.value = false
                        }
                    )
                }
            }

            composable<AddAccount>(
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                AddMoneySource(
                    navigateBack = { navController.navigateUp() }
                )
            }

            composable<EditAccount>(
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                EditMoneySource(
                    navigateBack = { navController.navigateUp() },
                    initialColor = it.toRoute<EditAccount>().paleColor
                )
            }

            composable<Settings> {
                SettingsMain(
                    viewModel = settingsViewModel,
                    bottomPadding = bottomNavPadding
                )
            }
        }
    }

}