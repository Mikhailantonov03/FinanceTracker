package com.devautro.financetracker.feature_moneySource.domain.repository

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import kotlinx.coroutines.flow.Flow

interface MoneySourceRepository {

    fun getMoneySources(): Flow<List<MoneySource>>

    suspend fun getMoneySourceById(id: Long): MoneySource?

    suspend fun insertMoneySource(moneySource: MoneySource)

    suspend fun updateMoneySource(moneySource: MoneySource)

    suspend fun deleteMoneySource(moneySource: MoneySource)

    suspend fun deleteAllMoneySources()
}