package com.devautro.financetracker.feature_payment.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments_table")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val date: Long,
    val monthTag: String,
    val description: String,
    val amountBefore: Double,
    val amountNew: Double,
    val isExpense: Boolean,
    val sourceId: Long? = null
)
