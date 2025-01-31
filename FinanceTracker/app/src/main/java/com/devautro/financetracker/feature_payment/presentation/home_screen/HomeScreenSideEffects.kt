package com.devautro.financetracker.feature_payment.presentation.home_screen

import com.devautro.financetracker.core.util.UiText

sealed class HomeScreenSideEffects {
    data object IncomesClick : HomeScreenSideEffects()
    data object ExpensesClick : HomeScreenSideEffects()
    data class ShowSnackbar(val message: UiText) : HomeScreenSideEffects()
}