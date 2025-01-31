package com.devautro.financetracker.feature_payment.domain.use_case

import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {

    suspend operator fun invoke(id: Long): Payment? {
        return repository.getPaymentById(id = id)
    }
}