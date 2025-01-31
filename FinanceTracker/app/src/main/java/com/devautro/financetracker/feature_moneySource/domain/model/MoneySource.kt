package com.devautro.financetracker.feature_moneySource.domain.model

data class MoneySource(
    val id: Long? = null,
    val name: String,
    val amount: Double,
    val includeInTotal: Boolean = true,
    val paleColor: Int,
    val accentColor: Int
)
