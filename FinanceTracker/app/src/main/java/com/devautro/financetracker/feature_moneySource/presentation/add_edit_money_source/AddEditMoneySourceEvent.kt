package com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source

sealed class AddEditMoneySourceEvent {
    data class SourceNameChanged(val name: String) : AddEditMoneySourceEvent()
    data class SourceAmountChanged(val amount: String) : AddEditMoneySourceEvent()
    data class NewColorPicked(val paleColor: Int, val accentColor: Int) : AddEditMoneySourceEvent()
    data object IncludeInTotalToggled : AddEditMoneySourceEvent()
    data object CancelButtonClicked : AddEditMoneySourceEvent()
    data object ApproveButtonClicked : AddEditMoneySourceEvent()
}