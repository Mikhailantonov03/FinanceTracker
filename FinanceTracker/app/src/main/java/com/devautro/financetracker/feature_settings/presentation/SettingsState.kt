package com.devautro.financetracker.feature_settings.presentation

import androidx.annotation.DrawableRes
import com.devautro.financetracker.core.util.Const
import java.util.Locale

data class SettingsState(
    val locale: String = Locale.getDefault().language,
    @DrawableRes val selectedLanguageImageId: Int = Const.flags.first { it.localeLanguage == locale }.imageResource,
    val isLanguageMenuShown: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isDeletePaymentsPicked: Boolean = false,
    val isDeleteMoneySourcesPicked: Boolean = false,
    val isDarkTheme: Boolean = true,
    val isLoading: Boolean = true,
    val isCurrenciesVisible: Boolean = false,
    val selectedCurrency: String = ""
)