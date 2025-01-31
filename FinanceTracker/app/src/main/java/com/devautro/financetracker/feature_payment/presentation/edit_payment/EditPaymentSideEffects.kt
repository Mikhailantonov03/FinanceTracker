package com.devautro.financetracker.feature_payment.presentation.edit_payment

import com.devautro.financetracker.core.util.UiText

sealed class EditPaymentSideEffects {
    data object CancelButton : EditPaymentSideEffects()
    data object SaveButton : EditPaymentSideEffects()
    data class ShowSnackbar(val message: UiText) : EditPaymentSideEffects()
}