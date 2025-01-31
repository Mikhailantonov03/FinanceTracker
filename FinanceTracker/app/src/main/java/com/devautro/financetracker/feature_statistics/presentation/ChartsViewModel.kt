package com.devautro.financetracker.feature_statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.GroupBar
import com.devautro.financetracker.R
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.core.util.formatDoubleToString
import com.devautro.financetracker.feature_payment.domain.use_case.PaymentUseCases
import com.devautro.financetracker.feature_payment.util.convertDateToMillis
import com.devautro.financetracker.feature_payment.util.convertMillisToDate
import com.devautro.financetracker.feature_statistics.util.convertMillisToMonthYear
import com.devautro.financetracker.feature_statistics.util.convertMonthYearToMillis
import com.devautro.financetracker.ui.theme.ExpenseRed
import com.devautro.financetracker.ui.theme.IncomeGreen
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
class ChartsViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases
) : ViewModel() {

    private val _chartsState = MutableStateFlow(ChartsState())
    val chartsState: StateFlow<ChartsState> = _chartsState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<ChartsSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        getAllData()
    }

    fun onEvent(event: ChartsEvent) {
        when (event) {
            is ChartsEvent.PeriodTabClick -> {
                _chartsState.update { state ->
                    state.copy(
                        selectedPeriodIndex = event.index
                    )
                }
                when (event.index) {
                    0 -> {
                        getFilteredDataByWeek()
                    }

                    1 -> {
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
                    val monthIncomes = incomes.filter {
                        val oneWeekAgo = LocalDate.now().minus(1, ChronoUnit.WEEKS)
                        val oneWeekAgoMillis = oneWeekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        it.date!! > oneWeekAgoMillis
                    }

                    val monthExpenses = expenses.filter {
                        val oneWeekAgo = LocalDate.now().minus(1, ChronoUnit.WEEKS)
                        val oneWeekAgoMillis = oneWeekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        it.date!! > oneWeekAgoMillis
                    }

                    val monthIncomesMap = monthIncomes
                        .groupBy { convertMillisToDate(it.date!!) }
                        .map { it.key to it.value.sumOf { payment -> payment.amountNew!! } }.toMap()

                    val monthExpensesMap = monthExpenses
                        .groupBy { convertMillisToDate(it.date!!) }
                        .map { it.key to it.value.sumOf { payment -> payment.amountNew!! } }.toMap()

                    monthIncomesMap to monthExpensesMap
                }.collect { (monthIncomes, monthExpenses) ->
                    if (monthIncomes.values.isEmpty() && monthExpenses.values.isEmpty()) {
                        resetChartsState()
                        return@collect
                    }

                    val incomesSum = monthIncomes.values.sum()
                    val expensesSum = monthExpenses.values.sum()
                    val max = (monthIncomes.values + monthExpenses.values).max()

                    val allKeys = (monthIncomes.keys + monthExpenses.keys).distinct()

                    val combinedMap = allKeys.associateWith { key ->
                        val incomes = monthIncomes[key]
                        val expenses = monthExpenses[key]
                        Pair(incomes, expenses)
                    }

                    val groupedBarList = combinedMap.toList()
                        .sortedBy { pair ->
                            convertDateToMillis(pair.first)
                        }.mapIndexed { index, value ->
                            val amount = value.second
                            val x = index.toFloat()

                            GroupBar(
                                label = value.first,
                                barList = listOf(
                                    BarData(
                                        point = Point(x = x, y = amount.first?.toFloat() ?: 0f),
                                        color = IncomeGreen
                                    ),
                                    BarData(
                                        point = Point(x = x, y = amount.second?.toFloat() ?: 0f),
                                        color = ExpenseRed
                                    )
                                )
                            )
                        }

                    _chartsState.update { state ->
                        state.copy(
                            selectedPeriodIndex = 0,
                            incomesSum = formatDoubleToString(incomesSum),
                            expensesSum = formatDoubleToString(expensesSum),
                            maxAmount = max,
                            groupBarList = groupedBarList
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
                    val monthIncomesMap = incomes.filter {
                        val oneMonthAgo = LocalDate.now().minus(1, ChronoUnit.MONTHS)
                        val oneMonthAgoMillis =
                            oneMonthAgo.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                .toEpochMilli()
                        it.date!! > oneMonthAgoMillis
                    }.groupBy { convertMillisToDate(it.date!!) }
                        .map { it.key to it.value.sumOf { payment -> payment.amountNew!! } }.toMap()


                    val monthExpensesMap = expenses.filter {
                        val oneMonthAgo = LocalDate.now().minus(1, ChronoUnit.MONTHS)
                        val oneMonthAgoMillis =
                            oneMonthAgo.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                .toEpochMilli()
                        it.date!! > oneMonthAgoMillis
                    }.groupBy { convertMillisToDate(it.date!!) }
                        .map { it.key to it.value.sumOf { payment -> payment.amountNew!! } }.toMap()

                    monthIncomesMap to monthExpensesMap

                }.collect { (monthIncomes, monthExpenses) ->
                    if (monthIncomes.values.isEmpty() && monthExpenses.values.isEmpty()) {
                        resetChartsState()
                        return@collect
                    }

                    val incomesSum = monthIncomes.values.sum()
                    val expensesSum = monthExpenses.values.sum()
                    val max = (monthIncomes.values + monthExpenses.values).max()

                    val allKeys = (monthIncomes.keys + monthExpenses.keys).distinct()

                    val combinedMap = allKeys.associateWith { key ->
                        val incomes = monthIncomes[key]
                        val expenses = monthExpenses[key]
                        Pair(incomes, expenses)
                    }

                    val groupedBarList = combinedMap.toList()
                        .sortedBy { pair ->
                            convertDateToMillis(pair.first)
                        }.mapIndexed { index, value ->
                            val amount = value.second
                            val x = index.toFloat()

                            GroupBar(
                                label = value.first,
                                barList = listOf(
                                    BarData(
                                        point = Point(x = x, y = amount.first?.toFloat() ?: 0f),
                                        color = IncomeGreen
                                    ),
                                    BarData(
                                        point = Point(x = x, y = amount.second?.toFloat() ?: 0f),
                                        color = ExpenseRed
                                    )
                                )
                            )
                        }

                    _chartsState.update { state ->
                        state.copy(
                            selectedPeriodIndex = 1,
                            incomesSum = formatDoubleToString(incomesSum),
                            expensesSum = formatDoubleToString(expensesSum),
                            maxAmount = max,
                            groupBarList = groupedBarList
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

                    val incomesSumOfMonthAndYear = incomes.groupBy { payment ->

                        convertMillisToMonthYear(payment.date!!)
                    }.map {
                        it.key to it.value.sumOf { payment -> payment.amountNew!! }
                    }.toMap()

                    val expensesSumOfMonthAndYear = expenses.groupBy { payment ->

                        convertMillisToMonthYear(payment.date!!)
                    }.map {
                        it.key to it.value.sumOf { payment -> payment.amountNew!! }
                    }.toMap()

                    incomesSumOfMonthAndYear to expensesSumOfMonthAndYear
                }.collect { (incomesMap, expensesMap) ->
                    if (incomesMap.values.isEmpty() && expensesMap.values.isEmpty()) {
                        resetChartsState()
                        return@collect
                    }

                    val incomesSum = incomesMap.values.sum()
                    val expensesSum = expensesMap.values.sum()
                    val max = (incomesMap.values + expensesMap.values).max()

                    val allKeys = (incomesMap.keys + expensesMap.keys).distinct()

                    val combinedMap = allKeys.associateWith { key ->
                        val income = incomesMap[key]
                        val expense = expensesMap[key]
                        Pair(income, expense)
                    }


                    val groupedBarList = combinedMap.toList()
                        .sortedBy { pair ->
                            convertMonthYearToMillis(pair.first)
                        }
                        .mapIndexed { index, value ->
                            val amount = value.second
                            val x = index.toFloat()

                            GroupBar(
                                label = value.first,
                                barList = listOf(
                                    BarData(
                                        point = Point(x = x, y = amount.first?.toFloat() ?: 0f),
                                        color = IncomeGreen
                                    ),
                                    BarData(
                                        point = Point(x = x, y = amount.second?.toFloat() ?: 0f),
                                        color = ExpenseRed
                                    )
                                )
                            )
                        }

                    _chartsState.update { state ->
                        state.copy(
                            selectedPeriodIndex = 2,
                            incomesSum = formatDoubleToString(incomesSum),
                            expensesSum = formatDoubleToString(expensesSum),
                            groupBarList = groupedBarList,
                            maxAmount = max
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
            ChartsSideEffects.ShowSnackbar(
                message = UiText.StringResource(R.string.error_get_payments_data)
            )
        )
    }

    private fun resetChartsState() {
        _chartsState.update { state ->
            state.copy(
                maxAmount = 100.0,
                groupBarList = emptyList(),
                incomesSum = "",
                expensesSum = ""
            )
        }
    }

}