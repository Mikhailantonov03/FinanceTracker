package com.devautro.financetracker.feature_moneySource.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moneySources_table")
data class MoneySourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,
    val amount: Double,
    val includeInTotal: Boolean = true,
    val paleColor: Int,
    val accentColor: Int
)
