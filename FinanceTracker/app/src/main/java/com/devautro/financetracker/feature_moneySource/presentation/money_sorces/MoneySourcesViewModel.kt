package com.devautro.financetracker.feature_moneySource.presentation.money_sorces

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devautro.financetracker.R
import com.devautro.financetracker.core.util.Const
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.use_case.MoneySourceUseCases
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.mappers.toMoneySource
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.mappers.toMoneySourceItem
import com.devautro.financetracker.core.util.formatDoubleToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoneySourcesViewModel @Inject constructor(
    private val moneySourceUseCases: MoneySourceUseCases
) : ViewModel() {

    private val _moneySourcesState = MutableStateFlow(MoneySourcesState())
    val moneySourcesState: StateFlow<MoneySourcesState> = _moneySourcesState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<MoneySourcesSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        if (_moneySourcesState.value.isIncludedOnlyFilter) {
            getFilteredByIncludedData()
        } else {
            getInitialData()
        }
    }

    fun onEvent(event: MoneySourcesEvent) {
        when (event) {
            is MoneySourcesEvent.AddIconClick -> {
                viewModelScope.launch {
                    _sideEffects.emit(MoneySourcesSideEffects.NavigateAddScreen)
                }
            }

            is MoneySourcesEvent.DeleteApproval -> {
                val deleteItem = _moneySourcesState.value.deleteItem
                if (deleteItem != null) {
                    viewModelScope.launch {
                        try {
                            moneySourceUseCases.deleteMoneySourceUseCase(deleteItem.toMoneySource())
                        } catch (e: Exception) {
                            e.printStackTrace()

                            _sideEffects.emit(
                                MoneySourcesSideEffects.ShowSnackbar(
                                    message = UiText.StringResource(R.string.error_delete_ms)
                                )
                            )
                            return@launch
                        }
                        _moneySourcesState.update { state ->
                            state.copy(
                                deleteItem = null,
                                showDeleteDialog = false
                            )
                        }

                    }
                }
            }

            is MoneySourcesEvent.DeleteIconClick -> {
                _moneySourcesState.update { state ->
                    state.copy(
                        showDeleteDialog = true,
                        deleteItem = event.deleteSource
                    )
                }
            }

            is MoneySourcesEvent.DismissDeleteDialog -> {
                _moneySourcesState.update { state ->
                    state.copy(
                        showDeleteDialog = false
                    )
                }
            }

            is MoneySourcesEvent.EditIconClick -> {
                viewModelScope.launch {
                    _sideEffects.emit(
                        MoneySourcesSideEffects.NavigateEditScreen(
                            id = event.id,
                            paleColor = event.paleColor
                        )
                    )
                }
            }

            is MoneySourcesEvent.ItemRevealed -> {
                _moneySourcesState.update { state ->
                    state.copy(
                        moneySourceList = updateIsRevealedField(
                            targetList = state.moneySourceList,
                            newValue = event.isRevealed,
                            itemId = event.id
                        )
                    )
                }
            }

            is MoneySourcesEvent.SwitcherIconClick -> {
                _moneySourcesState.update { state ->
                    state.copy(
                        isTableFormatData = !state.isTableFormatData
                    )
                }
            }

            is MoneySourcesEvent.FilterIncludedClick -> {
                _moneySourcesState.update { state ->
                    state.copy(
                        isIncludedOnlyFilter = !state.isIncludedOnlyFilter
                    )
                }
                if (_moneySourcesState.value.isIncludedOnlyFilter) {
                    getFilteredByIncludedData()
                } else {
                    getInitialData()
                }
            }
        }
    }


    private fun getInitialData() {
        viewModelScope.launch {
            moneySourceUseCases.getAllMoneySourcesUseCase().collect { moneySourceList ->

                if (moneySourceList.isEmpty()) {
                    moneySourceUseCases.addMoneySourceUseCase(
                        moneySource = MoneySource(
                            name = "Main Source",
                            amount = 0.0,
                            paleColor = Const.sourcePaleColors[0].toArgb(),
                            accentColor = Const.sourceAccentColors[0].toArgb()
                        )
                    )
                }

                val moneySourceItemsList = moneySourceList.map { it.toMoneySourceItem() }
                val totalAmount = moneySourceItemsList.sumOf { it.amount }

                _moneySourcesState.update { state ->
                    state.copy(
                        moneySourceList = moneySourceItemsList,
                        totalAmount = formatDoubleToString(totalAmount)
                    )
                }

            }
        }
    }

    private fun getFilteredByIncludedData() {
        viewModelScope.launch {
            moneySourceUseCases.getAllMoneySourcesUseCase().collect { moneySourceList ->

                val moneySourceItemsList = moneySourceList
                    .filter { it.includeInTotal }
                    .map { it.toMoneySourceItem() }
                val totalAmount = moneySourceItemsList
                    .filter { it.includeInTotal }
                    .sumOf { it.amount }

                _moneySourcesState.update { state ->
                    state.copy(
                        moneySourceList = moneySourceItemsList,
                        totalAmount = formatDoubleToString(totalAmount),
                        isIncludedOnlyFilter = true
                    )
                }

            }
        }
    }

    private fun updateIsRevealedField(
        targetList: List<MoneySourceItem>,
        newValue: Boolean,
        itemId: Long
    ): List<MoneySourceItem> {
        val mList = mutableListOf<MoneySourceItem>()
        mList.addAll(targetList)

        return mList.map { item ->
            if (item.id == itemId) {
                item.copy(isRevealed = newValue)
            } else {
                item
            }
        }.toList()
    }

}