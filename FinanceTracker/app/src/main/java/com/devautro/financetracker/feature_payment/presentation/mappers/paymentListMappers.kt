package com.devautro.financetracker.feature_payment.presentation.mappers

import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.PaymentItem

fun Payment.toPaymentItem(): PaymentItem {
    return PaymentItem(
        id = id,
        date = date ?: 0L,
        monthTag = monthTag,
        description = description,
        amountBefore = amountBefore,
        amountNew = amountNew ?: 0.0,
        isExpense = isExpense,
        sourceId = sourceId,
        isRevealed = false
    )
}

fun PaymentItem.toPayment(): Payment {
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