package com.devautro.financetracker.feature_moneySource.domain.use_case

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.repository.MoneySourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMoneySourcesUseCase @Inject constructor(
    private val repository: MoneySourceRepository
) {

    operator fun invoke(): Flow<List<MoneySource>> {
        return repository.getMoneySources()
    }
}