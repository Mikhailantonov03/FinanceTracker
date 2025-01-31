package com.devautro.financetracker.feature_payment.presentation.edit_payment

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource

data class EditPaymentState(
    val isDatePickerVisible: Boolean = false,
    val isMonthTagMenuVisible: Boolean = false,
    val moneySourceList: List<MoneySource> = emptyList(),
    val selectedMoneySource: MoneySource? = null,
    val isMoneySourceMenuVisible: Boolean = false,
    val amountInString: String = ""
)
