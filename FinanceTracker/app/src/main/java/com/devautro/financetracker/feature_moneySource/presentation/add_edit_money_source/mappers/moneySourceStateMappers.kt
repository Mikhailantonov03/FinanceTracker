package com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.mappers

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.AddEditMoneySourceState
import com.devautro.financetracker.core.util.formatDoubleToString
import com.devautro.financetracker.core.util.formatStringToDouble

fun AddEditMoneySourceState.toMoneySource(): MoneySource {
    return MoneySource(
        id = id,
        name = name,
        amount = formatStringToDouble(amount),
        includeInTotal = includedInTotal,
        paleColor = paleColor,
        accentColor = accentColor
    )
}

fun MoneySource.toAddEditMoneySourceState(): AddEditMoneySourceState {
    return AddEditMoneySourceState(
        id = id,
        name = name,
        amount = formatDoubleToString(amount),
        includedInTotal = includeInTotal,
        paleColor = paleColor,
        accentColor = accentColor
    )
}