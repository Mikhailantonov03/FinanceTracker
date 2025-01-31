package com.devautro.financetracker.feature_moneySource.presentation.money_sorces.mappers

import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.MoneySourceItem

fun MoneySource.toMoneySourceItem(): MoneySourceItem {
    return MoneySourceItem(
        id = id,
        name = name,
        amount = amount,
        includeInTotal = includeInTotal,
        paleColor = paleColor,
        accentColor = accentColor,
        isRevealed = false
    )
}

fun MoneySourceItem.toMoneySource(): MoneySource {
    return MoneySource(
        id = id,
        name = name,
        amount = amount,
        includeInTotal = includeInTotal,
        paleColor = paleColor,
        accentColor = accentColor
    )
}