package com.devautro.financetracker.feature_payment.domain.use_case

import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Add filtering by MONTH_TAG,
 * by month, week ??? -->
 */

class GetIncomesUseCase @Inject constructor(
    private val repository: PaymentRepository
) {

    operator fun invoke(): Flow<List<Payment>> {
        return repository.getCertainTypeOfPayment(isExpense = false)
    }
}