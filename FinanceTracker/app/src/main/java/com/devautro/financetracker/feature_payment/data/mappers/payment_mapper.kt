package com.devautro.financetracker.feature_payment.data.mappers

import com.devautro.financetracker.feature_payment.data.model.PaymentEntity
import com.devautro.financetracker.feature_payment.domain.model.Payment

fun PaymentEntity.toPayment(): Payment {
    return Payment(
        id = id,
        date = date,
        monthTag = monthTag,
        description = description,
        amountBefore = amountBefore,
        amountNew = amountNew,
        isExpense = isExpense,
        sourceId = sourceId
    )
}

fun Payment.toPaymentEntity(): PaymentEntity {
    return PaymentEntity(
        id = id,
        date = date ?: System.currentTimeMillis(),
        monthTag = monthTag,
        description = description,
        amountBefore = amountBefore ?: 0.0,
        amountNew = amountNew ?: 0.0,
        isExpense = isExpense,
        sourceId = sourceId
    )
}