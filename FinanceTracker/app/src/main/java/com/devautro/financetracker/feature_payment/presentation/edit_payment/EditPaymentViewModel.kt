package com.devautro.financetracker.feature_payment.presentation.edit_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devautro.financetracker.R
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.use_case.MoneySourceUseCases
import com.devautro.financetracker.feature_payment.domain.model.InvalidPaymentException
import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.use_case.PaymentUseCases
import com.devautro.financetracker.core.util.formatStringToDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditPaymentViewModel @Inject constructor(
    private val moneySourceUseCases: MoneySourceUseCases,
    private val paymentUseCases: PaymentUseCases
) : ViewModel() {


    private val _paymentData = MutableStateFlow(Payment())
    val paymentData: StateFlow<Payment> = _paymentData.asStateFlow()

    private val _paymentState = MutableStateFlow(EditPaymentState())
    val paymentState: StateFlow<EditPaymentState> = _paymentState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<EditPaymentSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        viewModelScope.launch {
            moneySourceUseCases.getAllMoneySourcesUseCase().collect { moneySourceList ->



                if (moneySourceList.isNotEmpty()) {
                    _paymentState.update { state ->
                        state.copy(moneySourceList = moneySourceList)
                    }
                }
            }
        }
    }

    fun onEvent(event: EditPaymentEvent) {
        when (event) {
            is EditPaymentEvent.UpdateInitialPayment -> {
                _paymentData.update { payment ->
                    payment.copy(
                        id = event.initialPayment.id,
                        date = event.initialPayment.date,
                        monthTag = event.initialPayment.monthTag,
                        description = event.initialPayment.description,
                        amountBefore = event.initialPayment.amountNew,
                        amountNew = event.initialPayment.amountNew,
                        isExpense = event.initialPayment.isExpense,
                        sourceId = event.initialPayment.sourceId
                    )
                }

                _paymentState.update { state ->
                    state.copy(
                        amountInString = String.format(Locale.US, "%.2f", event.initialPayment.amountNew),
                        selectedMoneySource = _paymentState.value.moneySourceList.find {
                            it.id == event.initialPayment.sourceId
                        }
                    )
                }
            }

            is EditPaymentEvent.DateIconClick -> {
                _paymentState.update { state ->
                    state.copy(
                        isDatePickerVisible = true
                    )
                }
            }

            is EditPaymentEvent.DateSelected -> {
                _paymentData.update { payment ->
                    payment.copy(
                        date = event.date
                    )
                }
            }

            is EditPaymentEvent.DismissDatePicker -> {
                _paymentState.update { state ->
                    state.copy(
                        isDatePickerVisible = false
                    )
                }
            }

            is EditPaymentEvent.EnteredDescription -> {
                _paymentData.update { payment ->
                    payment.copy(
                        description = event.text
                    )
                }
            }

            is EditPaymentEvent.EnteredAmount -> {

                _paymentState.update { state ->
                    state.copy(
                        amountInString = event.amount
                    )
                }
            }

            is EditPaymentEvent.MonthTagIconClick -> {
                _paymentState.update { state ->
                    state.copy(
                        isMonthTagMenuVisible = true
                    )
                }
            }

            is EditPaymentEvent.MonthTagSelected -> {
                _paymentData.update { payment ->
                    payment.copy(
                        monthTag = event.monthTag
                    )
                }
            }

            is EditPaymentEvent.DismissMonthTagMenu -> {
                _paymentState.update { state ->
                    state.copy(
                        isMonthTagMenuVisible = false
                    )
                }
            }

            is EditPaymentEvent.MoneySourceIconClick -> {
                _paymentState.update { state ->
                    state.copy(
                        isMoneySourceMenuVisible = true
                    )
                }
            }

            is EditPaymentEvent.ClearChosenMoneySource -> {

                _paymentState.update { state ->
                    state.copy(
                        selectedMoneySource = null
                    )
                }
            }

            is EditPaymentEvent.MoneySourceSelected -> {


                _paymentState.update { state ->
                    state.copy(
                        selectedMoneySource = state.moneySourceList.find { moneySource ->
                            moneySource == event.moneySource
                        }
                    )
                }
            }

            is EditPaymentEvent.DismissMoneySourceMenu -> {
                _paymentState.update { state ->
                    state.copy(
                        isMoneySourceMenuVisible = false
                    )
                }
            }

            is EditPaymentEvent.CheckBoxSelected -> {
                _paymentData.update { payment ->
                    payment.copy(
                        isExpense = event.isExpense
                    )
                }
            }

            is EditPaymentEvent.SaveButtonClick -> {

                viewModelScope.launch(Dispatchers.IO) {
                    val payment = _paymentData.value.copy(
                        amountNew = formatStringToDouble(_paymentState.value.amountInString)
                    )
                    val selectedMs = _paymentState.value.selectedMoneySource

                    try {
                        paymentUseCases.editPaymentUseCase(
                            payment = payment.copy(sourceId = selectedMs?.id )
                        )
                    } catch (e: InvalidPaymentException) {
                        e.printStackTrace()

                        _sideEffects.emit(
                            EditPaymentSideEffects.ShowSnackbar(
                                message = UiText.StringResource(R.string.error_update_payment)
                            )
                        )
                        return@launch
                    }


                    try {
                        val deferred = async { moneySourceUseCases.getMoneySourceUseCase(id = payment.sourceId) }
                        val oldMoneySource = deferred.await()

                        updateMoneySourceAmount(
                            amountOld = payment.amountBefore,
                            amountNew = payment.amountNew,
                            msOld = oldMoneySource,
                            msNew = selectedMs,
                            isExpense = payment.isExpense
                        )

                    } catch (e: Exception) {
                        e.printStackTrace()

                        _sideEffects.emit(
                            EditPaymentSideEffects.ShowSnackbar(
                                message = UiText.StringResource(R.string.error_ms_amount_update)
                            )
                        )
                        return@launch
                    }
                    _sideEffects.emit(EditPaymentSideEffects.SaveButton)
                    clearSelectedData()
                }
            }

            is EditPaymentEvent.CancelButtonClick -> {
                viewModelScope.launch {
                    _sideEffects.emit(EditPaymentSideEffects.CancelButton)
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

    private suspend fun updateMoneySourceAmount(
        amountOld: Double?,
        amountNew: Double?,
        msOld: MoneySource?,
        msNew: MoneySource?,
        isExpense: Boolean
    ) {
        when {
            amountNew == amountOld && msNew == msOld -> return

            amountNew != amountOld && msNew == msOld -> {
                if (msNew == null || msOld == null || amountNew == null || amountOld == null) return

                val recalculatedMs = changeAmountWithSameMoneySource(
                    msOld = msOld,
                    amountOld = amountOld,
                    amountNew = amountNew,
                    isExpense = isExpense
                )

                moneySourceUseCases.editMoneySourceUseCase(moneySource = recalculatedMs)
            }

            amountNew == amountOld && msNew != msOld -> {
                if (amountNew == null) return

                changeMoneySourceWithSameAmount(
                    amountNew = amountNew,
                    msOld = msOld,
                    msNew = msNew,
                    isExpense = isExpense
                )
            }

            amountNew != amountOld && msNew != msOld -> {
                if (amountOld == null || amountNew == null) return
                changeMoneySourceWithDifferentAmount(
                    msOld = msOld,
                    msNew = msNew,
                    amountOld = amountOld,
                    amountNew = amountNew,
                    isExpense = isExpense
                )
            }
        }
    }

    private fun changeAmountWithSameMoneySource(
        msOld: MoneySource,
        amountOld: Double,
        amountNew: Double,
        isExpense: Boolean
    ): MoneySource {
        // isExpense - GOOD / !isExpense - GOOD
        return if (isExpense) {
            msOld.copy(
                amount = (msOld.amount + amountOld) - amountNew
            )
        } else {
            msOld.copy(
                amount = (msOld.amount - amountOld) + amountNew
            )
        }
    }

    private suspend fun changeMoneySourceWithSameAmount(
        amountNew: Double,
        msOld: MoneySource?,
        msNew: MoneySource?,
        isExpense: Boolean
    ) {
        when {

            msOld == null && msNew != null -> {
                if (isExpense) {
                    val ms = msNew.copy(amount = msNew.amount - amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                } else {
                    val ms = msNew.copy(amount = msNew.amount + amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                }
            }

            msOld != null && msNew == null -> {
                if (isExpense) {
                    val ms = msOld.copy(amount = msOld.amount + amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                } else {
                    val ms = msOld.copy(amount = msOld.amount - amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                }
            }


            msOld != null && msNew != null -> {
                if (isExpense) {
                    val newMs = msNew.copy(amount = msNew.amount - amountNew)
                    val oldMs = msOld.copy(amount = msOld.amount + amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = newMs)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = oldMs)
                } else {
                    val newMs = msNew.copy(amount = msNew.amount + amountNew)
                    val oldMs = msOld.copy(amount = msOld.amount - amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = newMs)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = oldMs)
                }
            }
            else -> return
        }
    }

    private suspend fun changeMoneySourceWithDifferentAmount(
        amountOld: Double,
        amountNew: Double,
        msOld: MoneySource?,
        msNew: MoneySource?,
        isExpense: Boolean
    ) {
        when {

            msOld == null && msNew != null -> {
                if (isExpense) {
                    val ms = msNew.copy(amount = msNew.amount - amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                } else {
                    val ms = msNew.copy(amount = msNew.amount + amountNew)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                }
            }

            msOld != null && msNew == null -> {
                if (isExpense) {
                    val ms = msOld.copy(amount = msOld.amount + amountOld)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                } else {
                    val ms = msOld.copy(amount = msOld.amount - amountOld)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = ms)
                }
            }

            msOld != null && msNew != null -> {
                if (isExpense) {
                    val newMs = msNew.copy(amount = msNew.amount - amountNew)
                    val oldMs = msOld.copy(amount = msOld.amount + amountOld)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = newMs)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = oldMs)
                } else {
                    val newMs = msNew.copy(amount = msNew.amount + amountNew)
                    val oldMs = msOld.copy(amount = msOld.amount - amountOld)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = newMs)
                    moneySourceUseCases.editMoneySourceUseCase(moneySource = oldMs)
                }
            }
            else -> return
        }
    }


}