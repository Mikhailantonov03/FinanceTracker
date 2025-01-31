package com.devautro.financetracker.feature_payment.presentation.home_screen

data class HomeScreenState(
    val incomesSum: String = "0",
    val expensesSum: String = "0",
    val budgetSum: String = "0",
    val selectedTabIndex: Int = 2
)
