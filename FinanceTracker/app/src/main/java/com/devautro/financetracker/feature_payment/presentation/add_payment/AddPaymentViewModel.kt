package com.devautro.financetracker.feature_payment.presentation.add_payment

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devautro.financetracker.R
import com.devautro.financetracker.core.util.Const
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.core.util.formatStringToDouble
import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.use_case.MoneySourceUseCases
import com.devautro.financetracker.feature_payment.domain.model.InvalidPaymentException
import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.use_case.PaymentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaymentViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases,
    private val moneySourceUseCases: MoneySourceUseCases
) : ViewModel() {


    private val _paymentData = MutableStateFlow(Payment())
    val paymentData: StateFlow<Payment> = _paymentData.asStateFlow()

    private val _paymentState = MutableStateFlow(AddPaymentState())
    val paymentState: StateFlow<AddPaymentState> = _paymentState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<AddPaymentSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        viewModelScope.launch {
            moneySourceUseCases.getAllMoneySourcesUseCase().collect { moneySourceList ->
                if (moneySourceList.isEmpty()) {
                    moneySourceUseCases.addMoneySourceUseCase(
                        moneySource = MoneySource(
                            name = "Main Source",
                            amount = 0.0,
                            paleColor = Const.sourcePaleColors[0].toArgb(),
                            accentColor = Const.sourceAccentColors[0].toArgb()
                        )
                    )
                }
                _paymentState.update { state ->
                    state.copy(moneySourceList = moneySourceList)
                }
            }
        }
    }

    fun onEvent(event: AddPaymentEvent) {
        when (event) {
            is AddPaymentEvent.DateIconClick -> {
                _paymentState.update { state ->
                    state.copy(
                        isDatePickerVisible = true
                    )
                }
            }

            is AddPaymentEvent.DateSelected -> {
                _paymentData.update { payment ->
                    payment.copy(
                        date = event.date
                    )
                }
            }

            is AddPaymentEvent.DismissDatePicker -> {
                _paymentState.update { state ->
                    state.copy(
                        isDatePickerVisible = false
                    )
                }
            }

            is AddPaymentEvent.EnteredDescription -> {
                _paymentData.update { payment ->
                    payment.copy(
                        description = event.text
                    )
                }
            }

            is AddPaymentEvent.EnteredAmount -> {
                try {
                    _paymentData.update { payment ->
                        payment.copy(
                            amountNew = formatStringToDouble(event.amount)
                        )
                    }
                } catch (e: NumberFormatException) {

                }

                _paymentState.update { state ->
                    state.copy(
                        amountInString = event.amount
                    )
                }
            }

            is AddPaymentEvent.MonthTagIconClick -> {
                _paymentState.update { state ->
                    state.copy(
                        isMonthTagMenuVisible = true
                    )
                }
            }

            is AddPaymentEvent.MonthTagSelected -> {
                _paymentData.update { payment ->
                    payment.copy(
                        monthTag = event.monthTag
                    )
                }
            }

            is AddPaymentEvent.DismissMonthTagMenu -> {
                _paymentState.update { state ->
                    state.copy(
                        isMonthTagMenuVisible = false
                    )
                }
            }

            is AddPaymentEvent.MoneySourceIconClick -> {
                _paymentState.update { state ->
                    state.copy(
                        isMoneySourceMenuVisible = true
                    )
                }
            }
            is AddPaymentEvent.ClearChosenMoneySource -> {
                _paymentData.update { payment ->
                    payment.copy(
                        sourceId = null
                    )
                }
                _paymentState.update { state ->
                    state.copy(
                        selectedMoneySource = null
                    )
                }
            }
            is AddPaymentEvent.MoneySourceSelected -> {
                _paymentData.update { payment ->
                    payment.copy(
                        sourceId = event.moneySource.id
                    )
                }
                _paymentState.update { state ->
                    state.copy(
                        selectedMoneySource = state.moneySourceList.find { moneySource ->
                            moneySource == event.moneySource
                        }
                    )
                }
            }

            is AddPaymentEvent.DismissMoneySourceMenu -> {
                _paymentState.update { state ->
                    state.copy(
                        isMoneySourceMenuVisible = false
                    )
                }
            }

            is AddPaymentEvent.CheckBoxSelected -> {
                _paymentData.update { payment ->
                    payment.copy(
                        isExpense = event.isExpense
                    )
                }
            }

            is AddPaymentEvent.AddButtonClick -> {
                viewModelScope.launch {
                    try {
                        paymentUseCases.addPaymentUseCase(payment = _paymentData.value)
                    } catch (e: InvalidPaymentException) {
                        e.printStackTrace()

                        _sideEffects.emit(
                            AddPaymentSideEffects.ShowSnackbar(
                                message = UiText.StringResource(R.string.error_add_payment)
                            )
                        )
                        return@launch
                    }
                    try {
                        updateMoneySourceAmount()
                    } catch (e: Exception) {
                        e.printStackTrace()

                        _sideEffects.emit(
                            AddPaymentSideEffects.ShowSnackbar(
                                message = UiText.StringResource(R.string.error_ms_amount_update)
                            )
                        )
                        return@launch
                    }
                    _sideEffects.emit(AddPaymentSideEffects.AddButton)
                    clearSelectedData()
                }
            }

            is AddPaymentEvent.CancelButtonClick -> {
                viewModelScope.launch {
                    _sideEffects.emit(AddPaymentSideEffects.CancelButton)
                }
                clearSelectedData()
            }
        }
    }

    private fun clearSelectedData() {
        _paymentData.update { payment ->
            payment.copy(
                date = null,
                description = "",
                amountNew = null,
                monthTag = "",
                isExpense = true,
                sourceId = null
            )
        }

        _paymentState.update { state ->
            state.copy(
                amountInString = "",
                selectedMoneySource = null
            )
        }
    }

    private suspend fun updateMoneySourceAmount() {

        var moneySource = _paymentState.value.selectedMoneySource
        val correctingValue = _paymentData.value.amountNew

        if (moneySource != null && correctingValue != null) {
            moneySource = when (_paymentData.value.isExpense) {
                true -> moneySource.copy(amount = moneySource.amount - correctingValue)
                false -> moneySource.copy(amount = moneySource.amount + correctingValue)
            }

            moneySourceUseCases.editMoneySourceUseCase(moneySource = moneySource)
        }

    }

}