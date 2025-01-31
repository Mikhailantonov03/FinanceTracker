package com.devautro.financetracker.feature_moneySource.data.repository

import com.devautro.financetracker.feature_moneySource.data.data_source.MoneySourceDao
import com.devautro.financetracker.feature_moneySource.data.mappers.toMoneySource
import com.devautro.financetracker.feature_moneySource.data.mappers.toMoneySourceEntity
import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.repository.MoneySourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoneySourceRepositoryImpl @Inject constructor(
    private val dao: MoneySourceDao
): MoneySourceRepository {

    override fun getMoneySources(): Flow<List<MoneySource>> {
        return dao.getMoneySources().map { moneySourcesEntities ->
            moneySourcesEntities.map { entity ->
                entity.toMoneySource()
            }
        }
    }

    override suspend fun getMoneySourceById(id: Long): MoneySource? {
        return dao.getMoneySourceById(id = id)?.toMoneySource()
    }

    override suspend fun insertMoneySource(moneySource: MoneySource) {
        dao.insertMoneySource(moneySourceEntity = moneySource.toMoneySourceEntity())
    }

    override suspend fun updateMoneySource(moneySource: MoneySource) {
        dao.updateMoneySource(moneySourceEntity = moneySource.toMoneySourceEntity())
    }

    override suspend fun deleteMoneySource(moneySource: MoneySource) {
        dao.deleteMoneySource(moneySourceEntity = moneySource.toMoneySourceEntity())
    }

    override suspend fun deleteAllMoneySources() {
        dao.deleteAllMoneySources()
    }

}