package com.devautro.financetracker.feature_payment.domain.use_case

import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.repository.PaymentRepository
import javax.inject.Inject

class DeletePaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {

    suspend operator fun invoke(payment: Payment) {
        repository.deletePayment(payment)
    }
}