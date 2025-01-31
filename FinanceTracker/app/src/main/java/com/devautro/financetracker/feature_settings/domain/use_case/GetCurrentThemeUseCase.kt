package com.devautro.financetracker.feature_settings.domain.use_case

import com.devautro.financetracker.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(): Flow<Boolean> {
        return repository.getCurrentTheme()
    }

}