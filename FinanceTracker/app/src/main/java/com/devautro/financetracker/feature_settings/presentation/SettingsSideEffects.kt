package com.devautro.financetracker.feature_settings.presentation

import com.devautro.financetracker.core.util.UiText

sealed class SettingsSideEffects {
    data class ShowSnackbar(val message: UiText) : SettingsSideEffects()
}