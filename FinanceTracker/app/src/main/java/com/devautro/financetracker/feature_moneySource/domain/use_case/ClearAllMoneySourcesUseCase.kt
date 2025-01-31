package com.devautro.financetracker.feature_moneySource.domain.use_case

import com.devautro.financetracker.feature_moneySource.domain.repository.MoneySourceRepository

class ClearAllMoneySourcesUseCase(
    private val repository: MoneySourceRepository
) {

    suspend operator fun invoke() {
        repository.deleteAllMoneySources()
    }

}