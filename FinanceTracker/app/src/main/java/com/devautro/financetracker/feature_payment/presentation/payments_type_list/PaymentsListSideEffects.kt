package com.devautro.financetracker.feature_payment.presentation.payments_type_list

import com.devautro.financetracker.core.util.UiText

sealed class PaymentsListSideEffects {
    data object NavigateBack : PaymentsListSideEffects()
    data class ShowSnackBar(
        val message: UiText,
        val snackbarButtonText: UiText? = null
    ) : PaymentsListSideEffects()
}