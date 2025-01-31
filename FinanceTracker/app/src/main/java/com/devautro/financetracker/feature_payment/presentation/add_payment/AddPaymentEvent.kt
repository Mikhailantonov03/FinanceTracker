package com.devautro.financetracker.feature_payment.presentation.add_payment

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource

sealed class AddPaymentEvent {
    data object DateIconClick : AddPaymentEvent()
    data class DateSelected(val date: Long) : AddPaymentEvent()
    data object DismissDatePicker : AddPaymentEvent()
    data class EnteredDescription(val text: String) : AddPaymentEvent()
    data class EnteredAmount(val amount: String) : AddPaymentEvent()
    data object MonthTagIconClick : AddPaymentEvent()
    data class MonthTagSelected(val monthTag: String) : AddPaymentEvent()
    data object DismissMonthTagMenu : AddPaymentEvent()
    data object MoneySourceIconClick : AddPaymentEvent()
    data object ClearChosenMoneySource : AddPaymentEvent()
    data class MoneySourceSelected(val moneySource: MoneySource) : AddPaymentEvent()
    data object DismissMoneySourceMenu : AddPaymentEvent()
    data class CheckBoxSelected(val isExpense: Boolean) : AddPaymentEvent()
    data object AddButtonClick : AddPaymentEvent()
    data object CancelButtonClick : AddPaymentEvent()
}