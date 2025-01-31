package com.devautro.financetracker.feature_statistics.presentation

import co.yml.charts.ui.barchart.models.GroupBar

data class ChartsState(
    val selectedPeriodIndex: Int = 2,
    val groupBarList: List<GroupBar> = emptyList(),
    val maxAmount: Double = 100.0,
    val incomesSum: String = "",
    val expensesSum: String = "",
)
