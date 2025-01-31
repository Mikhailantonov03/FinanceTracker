package com.devautro.financetracker.feature_settings.domain.use_case

import com.devautro.financetracker.feature_settings.domain.repository.SettingsRepository
import javax.inject.Inject

class ChangeCurrencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(newSign: String) {
        repository.changeCurrency(sign = newSign)
    }

}