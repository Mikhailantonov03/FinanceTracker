package com.devautro.financetracker.feature_payment.data.repository

import com.devautro.financetracker.feature_payment.data.data_source.PaymentDao
import com.devautro.financetracker.feature_payment.data.mappers.toPayment
import com.devautro.financetracker.feature_payment.data.mappers.toPaymentEntity
import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val dao: PaymentDao
) : PaymentRepository {
    override fun getCertainTypeOfPayment(isExpense: Boolean): Flow<List<Payment>> {
        return dao.getCertainTypeOfPayments(isExpense = isExpense).map { paymentEntities ->
            paymentEntities
                .sortedByDescending { it.date }

                .map { entity ->
                entity.toPayment()
            }
        }
    }

    override suspend fun getPaymentById(id: Long): Payment? {
        return dao.getPaymentById(id = id)?.toPayment()
    }

    override suspend fun insertPayment(payment: Payment) {
        dao.insertPayment(paymentEntity = payment.toPaymentEntity())
    }

    override suspend fun deletePayment(payment: Payment) {
        dao.deletePayment(paymentEntity = payment.toPaymentEntity())
    }

    override suspend fun updatePayment(payment: Payment) {
        dao.updatePayment(paymentEntity = payment.toPaymentEntity())
    }

    override suspend fun deleteAllPayments() {
        dao.deleteAllPayments()
    }

}