package com.devautro.financetracker.feature_payment.domain.use_case

import com.devautro.financetracker.feature_payment.domain.model.InvalidPaymentException
import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.repository.PaymentRepository

class EditPaymentUseCase(
    private val repository: PaymentRepository
) {

    suspend operator fun invoke(payment: Payment) {
        if (payment.description.isBlank()) {
            throw InvalidPaymentException(message = "You have to enter a description")
        }
        if (payment.amountNew == null) {
            throw InvalidPaymentException(message = "Amount is null!")
        } else if (payment.amountNew <= 0.0) {
            throw InvalidPaymentException(message = "Amount should be greater than 0")
        }

        repository.updatePayment(payment = payment)
    }
}