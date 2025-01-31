package com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source

import com.devautro.financetracker.core.util.UiText

sealed class AddEditSourceSideEffects {
    data object CancelButtonClicked : AddEditSourceSideEffects()
    data object ApproveButtonClicked : AddEditSourceSideEffects()
    data class Showsnackbar(val message: UiText) : AddEditSourceSideEffects()
}