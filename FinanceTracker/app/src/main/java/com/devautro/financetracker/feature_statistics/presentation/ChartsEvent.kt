package com.devautro.financetracker.feature_statistics.presentation

sealed class ChartsEvent {
    data class PeriodTabClick(val index: Int) : ChartsEvent()
}