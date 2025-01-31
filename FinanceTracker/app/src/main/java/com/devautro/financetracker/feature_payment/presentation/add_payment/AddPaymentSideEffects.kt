package com.devautro.financetracker.feature_payment.presentation.add_payment

import com.devautro.financetracker.core.util.UiText

sealed class AddPaymentSideEffects {
    data object CancelButton : AddPaymentSideEffects()
    data object AddButton : AddPaymentSideEffects()
    data class ShowSnackbar(val message: UiText) : AddPaymentSideEffects()
}