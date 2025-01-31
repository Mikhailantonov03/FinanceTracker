package com.devautro.financetracker.feature_moneySource.presentation.money_sorces

sealed class MoneySourcesEvent {
    data object AddIconClick : MoneySourcesEvent()
    data object SwitcherIconClick : MoneySourcesEvent()
    data class DeleteIconClick(val deleteSource: MoneySourceItem) : MoneySourcesEvent()
    data object DeleteApproval : MoneySourcesEvent()
    data object DismissDeleteDialog : MoneySourcesEvent()
    data object FilterIncludedClick : MoneySourcesEvent()
    data class EditIconClick(val id: Long, val paleColor: Int) : MoneySourcesEvent()
    data class ItemRevealed(val id: Long, val isRevealed: Boolean) : MoneySourcesEvent()
}