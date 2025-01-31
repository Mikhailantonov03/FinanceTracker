package com.devautro.financetracker.feature_settings.domain.use_case

import com.devautro.financetracker.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class ChangeCurrentThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(isDarkTheme: Boolean) {
        repository.changeCurrentTheme(isDarkTheme = isDarkTheme)
    }

}