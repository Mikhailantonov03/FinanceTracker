package com.devautro.financetracker.feature_moneySource.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devautro.financetracker.feature_moneySource.data.model.MoneySourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoneySourceDao {

    @Query("SELECT * FROM moneySources_table")
    fun getMoneySources(): Flow<List<MoneySourceEntity>>

    @Query("SELECT * FROM moneySources_table WHERE id = :id")
    suspend fun getMoneySourceById(id: Long): MoneySourceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneySource(moneySourceEntity: MoneySourceEntity)

    @Update
    suspend fun updateMoneySource(moneySourceEntity: MoneySourceEntity)

    @Delete
    suspend fun deleteMoneySource(moneySourceEntity: MoneySourceEntity)

    @Query("DELETE FROM moneySources_table WHERE id NOT IN (SELECT id FROM moneySources_table ORDER BY id LIMIT 1)")
    suspend fun deleteAllMoneySources()
}