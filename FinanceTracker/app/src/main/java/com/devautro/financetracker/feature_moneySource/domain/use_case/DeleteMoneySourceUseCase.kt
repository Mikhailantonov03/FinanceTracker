package com.devautro.financetracker.feature_moneySource.domain.use_case

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.repository.MoneySourceRepository
import javax.inject.Inject

class DeleteMoneySourceUseCase @Inject constructor(
    private val repository: MoneySourceRepository
) {

    suspend operator fun invoke(moneySource: MoneySource) {
        repository.deleteMoneySource(moneySource = moneySource)
    }
}