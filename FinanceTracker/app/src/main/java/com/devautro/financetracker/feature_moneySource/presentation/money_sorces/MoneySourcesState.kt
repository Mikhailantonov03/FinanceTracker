package com.devautro.financetracker.feature_moneySource.presentation.money_sorces

data class MoneySourcesState(
    val moneySourceList: List<MoneySourceItem> = emptyList(),
    val isTableFormatData: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isIncludedOnlyFilter: Boolean = false,
    val totalAmount: String = "",
    val deleteItem: MoneySourceItem? = null
)
