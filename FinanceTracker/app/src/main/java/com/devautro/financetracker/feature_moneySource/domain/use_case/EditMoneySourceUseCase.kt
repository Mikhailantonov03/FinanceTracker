package com.devautro.financetracker.feature_moneySource.domain.use_case

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.repository.MoneySourceRepository

class EditMoneySourceUseCase(
    private val repository: MoneySourceRepository
) {

    suspend operator fun invoke(moneySource: MoneySource) {
        repository.updateMoneySource(moneySource = moneySource)
    }

}