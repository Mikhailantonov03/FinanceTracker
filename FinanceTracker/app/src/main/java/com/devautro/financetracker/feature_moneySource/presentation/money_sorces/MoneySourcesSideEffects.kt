package com.devautro.financetracker.feature_moneySource.presentation.money_sorces

import com.devautro.financetracker.core.util.UiText

sealed class MoneySourcesSideEffects {
    data class ShowSnackbar(val message: UiText) : MoneySourcesSideEffects()
    data object NavigateAddScreen : MoneySourcesSideEffects()
    data class NavigateEditScreen(val id: Long, val paleColor: Int) : MoneySourcesSideEffects()
}