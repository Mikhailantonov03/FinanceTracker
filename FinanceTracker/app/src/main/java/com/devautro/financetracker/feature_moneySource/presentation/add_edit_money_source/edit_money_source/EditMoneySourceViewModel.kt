package com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.edit_money_source

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.devautro.financetracker.R
import com.devautro.financetracker.core.presentation.navigation.EditAccount
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.feature_moneySource.domain.use_case.MoneySourceUseCases
import com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.AddEditMoneySourceEvent
import com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.AddEditMoneySourceState
import com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.AddEditSourceSideEffects
import com.devautro.financetracker.feature_moneySource.presentation.add_edit_money_source.mappers.toMoneySource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditMoneySourceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val moneySourceUseCases: MoneySourceUseCases
) : ViewModel() {

    private val _editMoneySourceState = MutableStateFlow(AddEditMoneySourceState())
    val editMoneySourceState: StateFlow<AddEditMoneySourceState> =
        _editMoneySourceState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<AddEditSourceSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        val route = savedStateHandle.toRoute<EditAccount>()
        _editMoneySourceState.update { state ->
            state.copy(
                id = route.id
            )
        }
        getInitialDataWithId(id = route.id)
    }

    fun onEvent(event: AddEditMoneySourceEvent) {
        when (event) {
            is AddEditMoneySourceEvent.IncludeInTotalToggled -> {
                _editMoneySourceState.update { state ->
                    state.copy(
                        includedInTotal = !state.includedInTotal
                    )
                }
            }

            is AddEditMoneySourceEvent.NewColorPicked -> {
                _editMoneySourceState.update { state ->
                    state.copy(
                        paleColor = event.paleColor,
                        accentColor = event.accentColor
                    )
                }
            }

            is AddEditMoneySourceEvent.SourceAmountChanged -> {


                _editMoneySourceState.update { state ->
                    state.copy(
                        amount = event.amount
                    )
                }
            }

            is AddEditMoneySourceEvent.SourceNameChanged -> {
                _editMoneySourceState.update { state ->
                    state.copy(
                        name = event.name
                    )
                }
            }

            is AddEditMoneySourceEvent.ApproveButtonClicked -> {
                viewModelScope.launch {
                    try {
                        moneySourceUseCases.editMoneySourceUseCase(
                            moneySource = _editMoneySourceState.value.toMoneySource()
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()

                        _sideEffects.emit(
                            AddEditSourceSideEffects.Showsnackbar(
                                message = UiText.StringResource(R.string.error_update_ms)
                            )
                        )
                        return@launch
                    }
                    _sideEffects.emit(AddEditSourceSideEffects.ApproveButtonClicked)
                }
            }

            is AddEditMoneySourceEvent.CancelButtonClicked -> {
                viewModelScope.launch {
                    _sideEffects.emit(AddEditSourceSideEffects.CancelButtonClicked)
                }
            }
        }
    }

    private fun getInitialDataWithId(id: Long) {
        viewModelScope.launch {
            val moneyS = moneySourceUseCases.getMoneySourceUseCase(id = id)

            if (moneyS != null) {
                _editMoneySourceState.update { state ->
                    state.copy(
                        name = moneyS.name,
                        amount = String.format(Locale.US, "%.2f", moneyS.amount),
                        paleColor = moneyS.paleColor,
                        accentColor = moneyS.accentColor,
                        includedInTotal = moneyS.includeInTotal
                    )
                }
            } else {
                _sideEffects.emit(
                    AddEditSourceSideEffects.Showsnackbar(
                        message = UiText.StringResource(R.string.error_ms_not_found)
                    )
                )
            }
        }
    }
}