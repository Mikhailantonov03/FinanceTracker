package com.devautro.financetracker.feature_payment.presentation.payments_type_list

data class PaymentItem(
    val id: Long? = null,
    val date: Long,
    val monthTag: String,
    val description: String,
    val amountBefore: Double? = null,
    val amountNew: Double,
    val isExpense: Boolean = true,
    val sourceId: Long? = null,
    val isRevealed: Boolean = false
)
