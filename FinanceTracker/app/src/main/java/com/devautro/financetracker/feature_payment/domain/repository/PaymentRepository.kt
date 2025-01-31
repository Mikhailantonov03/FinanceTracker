package com.devautro.financetracker.feature_payment.domain.repository

import com.devautro.financetracker.feature_payment.domain.model.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {

    fun getCertainTypeOfPayment(isExpense: Boolean): Flow<List<Payment>>

    suspend fun getPaymentById(id: Long): Payment?

    suspend fun insertPayment(payment: Payment)

    suspend fun deletePayment(payment: Payment)

    suspend fun updatePayment(payment: Payment)

    suspend fun deleteAllPayments()

}