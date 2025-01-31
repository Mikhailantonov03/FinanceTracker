package com.devautro.financetracker.feature_statistics.presentation

import com.devautro.financetracker.core.util.UiText

sealed class ChartsSideEffects {
    data class ShowSnackbar(val message: UiText) : ChartsSideEffects()
}