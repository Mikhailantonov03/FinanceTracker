package com.devautro.financetracker.feature_payment.domain.use_case

import com.devautro.financetracker.feature_payment.domain.repository.PaymentRepository

class ClearAllPaymentsUseCase(
    private val repository: PaymentRepository
) {

    suspend operator fun invoke() {
        repository.deleteAllPayments()
    }

}