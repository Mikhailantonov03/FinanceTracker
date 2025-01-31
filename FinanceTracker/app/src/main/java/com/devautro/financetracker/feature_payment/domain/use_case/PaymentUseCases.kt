package com.devautro.financetracker.feature_payment.domain.use_case

data class PaymentUseCases(
    val getIncomesUseCase: GetIncomesUseCase,
    val getExpensesUseCase: GetExpensesUseCase,
    val getPaymentUseCase: GetPaymentUseCase,
    val addPaymentUseCase: AddPaymentUseCase,
    val deletePaymentUseCase: DeletePaymentUseCase,
    val editPaymentUseCase: EditPaymentUseCase,
    val clearAllPaymentsUseCase: ClearAllPaymentsUseCase
)
