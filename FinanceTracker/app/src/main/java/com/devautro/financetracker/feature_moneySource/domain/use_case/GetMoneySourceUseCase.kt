package com.devautro.financetracker.feature_moneySource.domain.use_case

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.repository.MoneySourceRepository
import javax.inject.Inject

class GetMoneySourceUseCase @Inject constructor(
    private val repository: MoneySourceRepository
) {

    suspend operator fun invoke(id: Long?): MoneySource? {
        if (id == null) return null
        return repository.getMoneySourceById(id = id)
    }
}