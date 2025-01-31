package com.devautro.financetracker.feature_moneySource.presentation.money_sorces

import androidx.compose.ui.graphics.toArgb
import com.devautro.financetracker.core.util.Const

data class MoneySourceItem(
    val id: Long? = null,
    val name: String = "",
    val amount: Double = 0.0,
    val includeInTotal: Boolean = true,
    val paleColor: Int = Const.sourcePaleColors[currentIndex].toArgb(),
    val accentColor: Int = Const.sourceAccentColors[currentIndex].toArgb(),
    val isRevealed: Boolean = false
) {
    companion object {
        val currentIndex = (0..3).random()
    }
}
