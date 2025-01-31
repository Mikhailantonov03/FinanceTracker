package com.devautro.financetracker.feature_settings.domain.use_case

data class SettingsUseCases(
    val changeCurrentThemeUseCase: ChangeCurrentThemeUseCase,
    val getCurrentThemeUseCase: GetCurrentThemeUseCase,
    val changeCurrencyUseCase: ChangeCurrencyUseCase,
    val getChosenCurrencyUseCase: GetChosenCurrencyUseCase
)
