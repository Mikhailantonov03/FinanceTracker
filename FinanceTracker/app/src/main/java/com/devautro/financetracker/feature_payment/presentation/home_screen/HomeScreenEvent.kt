package com.devautro.financetracker.feature_payment.presentation.home_screen

sealed class HomeScreenEvent {
    data object IncomesClick : HomeScreenEvent()
    data object ExpensesClick : HomeScreenEvent()
    data class TabClick(val tabIndex: Int) : HomeScreenEvent()
}