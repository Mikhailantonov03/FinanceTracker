package com.devautro.financetracker.feature_payment.presentation.payments_type_list

import com.devautro.financetracker.feature_payment.domain.model.Payment

data class PaymentsListState(
    val paymentItemsList: List<PaymentItem> = emptyList(),
    val paymentYearsList: List<Int> = emptyList(),
    val totalAmount: String = "0",
    val isMonthTagMenuVisible: Boolean = false,
    val selectedMonthTag: String = "",

    val selectedYearIndex: Int = 0,
    val isEditBottomSheetVisible: Boolean = false,
    val paymentForSheet: Payment = Payment()
)
