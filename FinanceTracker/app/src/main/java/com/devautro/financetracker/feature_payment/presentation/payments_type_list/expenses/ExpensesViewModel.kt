package com.devautro.financetracker.feature_payment.presentation.payments_type_list.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devautro.financetracker.R
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.feature_moneySource.domain.use_case.MoneySourceUseCases
import com.devautro.financetracker.feature_payment.domain.model.Payment
import com.devautro.financetracker.feature_payment.domain.use_case.PaymentUseCases
import com.devautro.financetracker.feature_payment.presentation.mappers.toPayment
import com.devautro.financetracker.feature_payment.presentation.mappers.toPaymentItem
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.PaymentItem
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.PaymentsListEvent
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.PaymentsListSideEffects
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.PaymentsListState
import com.devautro.financetracker.core.util.formatDoubleToString
import com.devautro.financetracker.feature_payment.util.getYearOfTheDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases,
    private val moneySourceUseCases: MoneySourceUseCases
) : ViewModel() {

    private val _paymentsState = MutableStateFlow(PaymentsListState())
    val paymentsState: StateFlow<PaymentsListState> = _paymentsState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<PaymentsListSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    private var recentlyDeletedPayment: Payment? = null

    init {
        getInitialData()
    }

    fun onEvent(event: PaymentsListEvent) {
        when (event) {
            is PaymentsListEvent.ShowEditBottomSheet -> {
                _paymentsState.update { state ->
                    state.copy(
                        isEditBottomSheetVisible = false
                    )
                }
            }

            is PaymentsListEvent.FilterIconClick -> {
                _paymentsState.update { state ->
                    state.copy(
                        isMonthTagMenuVisible = true
                    )
                }
            }

            is PaymentsListEvent.MonthTagSelected -> {
                _paymentsState.update { state ->
                    state.copy(
                        selectedMonthTag = event.monthTag
                    )
                }
                getFilteredDataByMonthTag()
            }

            is PaymentsListEvent.CurrentYearSelected -> {
                getFilteredDataByMonthTag(
                    yearIndex = event.yearIndex
                )
            }

            is PaymentsListEvent.DismissMonthTagMenu -> {
                _paymentsState.update { state ->
                    state.copy(
                        isMonthTagMenuVisible = false
                    )
                }
            }

            is PaymentsListEvent.ClearIconCLick -> {
                _paymentsState.update { state ->
                    state.copy(
                        selectedMonthTag = ""
                    )
                }
                getInitialData()
            }

            is PaymentsListEvent.EditIconClick -> {
                _paymentsState.update { state ->
                    state.copy(
                        isEditBottomSheetVisible = true,
                        paymentForSheet = event.paymentItem.toPayment()
                    )
                }
            }

            is PaymentsListEvent.DeleteIconClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val payment = event.paymentItem.toPayment()
                        paymentUseCases.deletePaymentUseCase(
                            payment = payment
                        )
                        recentlyDeletedPayment = payment

                        updateMoneySourceAmountOfDeletedItem(payment = payment)

                        _sideEffects.emit(
                            PaymentsListSideEffects.ShowSnackBar(
                                message = UiText.StringResource(R.string.item_deleted),
                                snackbarButtonText = UiText.StringResource(R.string.undo_snackbar)
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()

                        _sideEffects.emit(PaymentsListSideEffects.ShowSnackBar(
                            message = UiText.StringResource(R.string.error_payment_delete)
                        ))
                    }
                }
            }

            is PaymentsListEvent.ItemRevealed -> {
                _paymentsState.update { state ->
                    state.copy(
                        paymentItemsList = updateIsRevealedField(
                            targetList = state.paymentItemsList,
                            newValue = event.isRevealed,
                            itemId = event.id
                        )
                    )
                }
            }

            is PaymentsListEvent.NavigateBack -> {
                viewModelScope.launch {
                    _sideEffects.emit(PaymentsListSideEffects.NavigateBack)
                }
            }

            PaymentsListEvent.RestorePayment -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        paymentUseCases.addPaymentUseCase(
                            payment = recentlyDeletedPayment ?: return@launch
                        )
                        updateMoneySourceAmountOfRestoredItem(
                            payment = recentlyDeletedPayment ?: return@launch
                        )
                        recentlyDeletedPayment = null
                    } catch (e: Exception) {
                        e.printStackTrace()

                        _sideEffects.emit(PaymentsListSideEffects.ShowSnackBar(
                            message = UiText.StringResource(R.string.error_payment_restore)
                        ))
                    }
                }
            }
        }
    }


    private fun getFilteredDataByMonthTag(
        yearIndex: Int = -1
    ) {
        viewModelScope.launch {
            paymentUseCases.getExpensesUseCase().collectLatest { expensesList ->
                val uniqueYears = expensesList.map {
                    getYearOfTheDate(it.date!!)
                }.toSet().sorted().toList()

                val lastYearIndex = if (yearIndex >= 0) yearIndex else uniqueYears.lastIndex

                val filteredExpensesList = expensesList.filter {
                    it.monthTag == _paymentsState.value.selectedMonthTag &&
                            getYearOfTheDate(it.date!!) == uniqueYears[lastYearIndex]
                }
                val filteredTotalAmount = filteredExpensesList.mapNotNull { it.amountNew }.sum()

                _paymentsState.update { state ->
                    state.copy(
                        paymentItemsList = filteredExpensesList
                            .map { payment ->
                                payment.toPaymentItem()
                            },
                        totalAmount = formatDoubleToString(filteredTotalAmount),
                        paymentYearsList = uniqueYears,
                        selectedYearIndex = lastYearIndex
                    )
                }
            }
        }
    }

    private fun getInitialData() {
        viewModelScope.launch {
            paymentUseCases.getExpensesUseCase().collectLatest { expensesList ->
                val totalAmount = expensesList
                    .mapNotNull { it.amountNew }
                    .sum()

                _paymentsState.update { state ->
                    state.copy(
                        paymentItemsList = expensesList
                            .map { payment ->
                                payment.toPaymentItem()
                            },
                        totalAmount = formatDoubleToString(totalAmount)
                    )
                }
            }
        }
    }

    private fun updateIsRevealedField(
        targetList: List<PaymentItem>,
        newValue: Boolean,
        itemId: Long
    ): List<PaymentItem> {
        val mList = mutableListOf<PaymentItem>()
        mList.addAll(targetList)

        return mList.map { item ->
            if (item.id == itemId) {
                item.copy(isRevealed = newValue)
            } else {
                item
            }
        }.toList()
    }

    private suspend fun updateMoneySourceAmountOfDeletedItem(
        payment: Payment
    ) {
        if (payment.sourceId == null || payment.amountNew == null) return
        val moneySource = moneySourceUseCases.getMoneySourceUseCase(id = payment.sourceId)

        moneySourceUseCases.editMoneySourceUseCase(
            moneySource = moneySource?.copy(

                amount = moneySource.amount + payment.amountNew
            ) ?: throw NullPointerException()
        )
    }

    private suspend fun updateMoneySourceAmountOfRestoredItem(
        payment: Payment
    ) {
        if (payment.sourceId == null || payment.amountNew == null) return
        val moneySource = moneySourceUseCases.getMoneySourceUseCase(id = payment.sourceId)

        moneySourceUseCases.editMoneySourceUseCase(
            moneySource = moneySource?.copy(

                amount = moneySource.amount - payment.amountNew
            ) ?: throw NullPointerException()
        )
    }

}