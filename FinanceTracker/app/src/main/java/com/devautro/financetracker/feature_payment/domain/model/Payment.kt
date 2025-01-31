package com.devautro.financetracker.feature_payment.domain.model

data class Payment(
    val id: Long? = null,
    val date: Long? = null,
    val monthTag: String= "",
    val description: String = "",
    val amountBefore: Double? = null,
    val amountNew: Double? = null,
    val isExpense: Boolean = true,
    val sourceId: Long? = null
)

class InvalidPaymentException(message: String): Exception(message)
