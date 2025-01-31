package com.devautro.financetracker.feature_payment.presentation.edit_payment

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_payment.domain.model.Payment

sealed class EditPaymentEvent {
    data class UpdateInitialPayment(val initialPayment: Payment) : EditPaymentEvent()
    data object DateIconClick : EditPaymentEvent()
    data class DateSelected(val date: Long) : EditPaymentEvent()
    data object DismissDatePicker : EditPaymentEvent()
    data class EnteredDescription(val text: String) : EditPaymentEvent()
    data class EnteredAmount(val amount: String) : EditPaymentEvent()
    data object MonthTagIconClick : EditPaymentEvent()
    data class MonthTagSelected(val monthTag: String) : EditPaymentEvent()
    data object DismissMonthTagMenu : EditPaymentEvent()
    data object MoneySourceIconClick : EditPaymentEvent()
    data object ClearChosenMoneySource : EditPaymentEvent()
    data class MoneySourceSelected(val moneySource: MoneySource) : EditPaymentEvent()
    data object DismissMoneySourceMenu : EditPaymentEvent()
    data class CheckBoxSelected(val isExpense: Boolean) : EditPaymentEvent()
    data object SaveButtonClick : EditPaymentEvent()
    data object CancelButtonClick : EditPaymentEvent()
}