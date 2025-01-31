package com.devautro.financetracker.feature_moneySource.data.mappers

import com.devautro.financetracker.feature_moneySource.data.model.MoneySourceEntity
import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource

fun MoneySourceEntity.toMoneySource(): MoneySource {
    return MoneySource(
        id = id,
        name = name,
        amount = amount,
        includeInTotal = includeInTotal,
        paleColor = paleColor,
        accentColor = accentColor
    )
}

fun MoneySource.toMoneySourceEntity(): MoneySourceEntity {
    return MoneySourceEntity(
        id = id,
        name = name,
        amount = amount,
        includeInTotal = includeInTotal,
        paleColor = paleColor,
        accentColor = accentColor
    )
}