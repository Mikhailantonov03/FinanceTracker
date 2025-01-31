package com.devautro.financetracker.feature_payment.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devautro.financetracker.R
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.feature_payment.domain.use_case.PaymentUseCases
import com.devautro.financetracker.core.util.formatDoubleToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeScreenState())
    val homeState: StateFlow<HomeScreenState> = _homeState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<HomeScreenSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        getAllData()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.IncomesClick -> {
                viewModelScope.launch {
                    _sideEffects.emit(HomeScreenSideEffects.IncomesClick)
                }
            }

            is HomeScreenEvent.ExpensesClick -> {
                viewModelScope.launch {
                    _sideEffects.emit(HomeScreenSideEffects.ExpensesClick)
                }
            }

            is HomeScreenEvent.TabClick -> {
                _homeState.update { state ->
                    state.copy(
                        selectedTabIndex = event.tabIndex
                    )
                }
                when (event.tabIndex) {
                    0 -> {
                        // filter by week
                        getFilteredDataByWeek()
                    }

                    1 -> {
                        // filter by month
                        getFilteredDataByMonth()
                    }

                    2 -> {
                        getAllData()
                    }
                }
            }
        }
    }

    private fun getFilteredDataByWeek() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                combine(
                    paymentUseCases.getIncomesUseCase(),
                    paymentUseCases.getExpensesUseCase()
                ) { incomes, expenses ->
                    val incomesSum = incomes.filter {
                        val oneWeekAgo = LocalDate.now().minus(1, ChronoUnit.WEEKS)
                        val oneWeekAgoMillis = oneWeekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        it.date!! > oneWeekAgoMillis
                    }.mapNotNull { it.amountNew }.sum()

                    val expensesSum = expenses.filter {
                        val oneWeekAgo = LocalDate.now().minus(1, ChronoUnit.WEEKS)
                        val oneWeekAgoMillis = oneWeekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        it.date!! > oneWeekAgoMillis
                    }.mapNotNull { it.amountNew }.sum()

                    incomesSum to expensesSum

                }.collect { (incomesSum, expensesSum) ->
                    val budgetSum = incomesSum - expensesSum

                    _homeState.update { state ->
                        state.copy(
                            incomesSum = formatDoubleToString(incomesSum),
                            expensesSum = formatDoubleToString(expensesSum),
                            budgetSum = formatDoubleToString(budgetSum)
                        )
                    }

                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun getFilteredDataByMonth() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                combine(
                    paymentUseCases.getIncomesUseCase(),
                    paymentUseCases.getExpensesUseCase()
                ) { incomes, expenses ->
                    val incomesSum = incomes.filter {
                        val oneMonthAgo = LocalDate.now().minus(1, ChronoUnit.MONTHS)
                        val oneMonthAgoMillis = oneMonthAgo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        it.date!! > oneMonthAgoMillis
                    }.mapNotNull { it.amountNew }.sum()

                    val expensesSum = expenses.filter {
                        val oneMonthAgo = LocalDate.now().minus(1, ChronoUnit.MONTHS)
                        val oneMonthAgoMillis = oneMonthAgo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        it.date!! > oneMonthAgoMillis
                    }.mapNotNull { it.amountNew }.sum()

                    incomesSum to expensesSum

                }.collect { (incomesSum, expensesSum) ->
                    val budgetSum = incomesSum - expensesSum

                    _homeState.update { state ->
                        state.copy(
                            incomesSum = formatDoubleToString(incomesSum),
                            expensesSum = formatDoubleToString(expensesSum),
                            budgetSum = formatDoubleToString(budgetSum)
                        )
                    }

                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                combine(
                    paymentUseCases.getIncomesUseCase(),
                    paymentUseCases.getExpensesUseCase()
                ) { incomes, expenses ->
                    val incomesSum = incomes.mapNotNull { it.amountNew }.sum()
                    val expensesSum = expenses.mapNotNull { it.amountNew }.sum()

                    incomesSum to expensesSum

                }.collect { (incomesSum, expensesSum) ->
                    val budgetSum = incomesSum - expensesSum

                    _homeState.update { state ->
                        state.copy(
                            incomesSum = formatDoubleToString(incomesSum),
                            expensesSum = formatDoubleToString(expensesSum),
                            budgetSum = formatDoubleToString(budgetSum)
                        )
                    }

                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun handleError(e: Exception) {
        e.printStackTrace()

        _sideEffects.emit(
            HomeScreenSideEffects.ShowSnackbar(
                message = UiText.StringResource(R.string.error_get_payments_data)
            )
        )
    }

}