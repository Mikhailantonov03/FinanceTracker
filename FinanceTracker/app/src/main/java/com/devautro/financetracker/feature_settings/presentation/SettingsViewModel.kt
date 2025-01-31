package com.devautro.financetracker.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devautro.financetracker.R
import com.devautro.financetracker.core.util.UiText
import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource
import com.devautro.financetracker.feature_moneySource.domain.use_case.MoneySourceUseCases
import com.devautro.financetracker.feature_payment.domain.use_case.PaymentUseCases
import com.devautro.financetracker.feature_settings.domain.use_case.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases,
    private val moneySourceUseCases: MoneySourceUseCases,
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<SettingsSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        getInitialTheme()
        getInitialCurrency()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.NewLanguageSelected -> {
                _settingsState.update { state ->
                    state.copy(
                        selectedLanguageImageId = event.flagImage,
                        locale = event.locale
                    )
                }
            }

            is SettingsEvent.ShowLanguageMenu -> {
                _settingsState.update { state ->
                    state.copy(
                        isLanguageMenuShown = true
                    )
                }
            }

            is SettingsEvent.DismissLanguageMenu -> {
                _settingsState.update { state ->
                    state.copy(
                        isLanguageMenuShown = false
                    )
                }
            }

            is SettingsEvent.SwitchTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        settingsUseCases.changeCurrentThemeUseCase(isDarkTheme = event.isDarkTheme)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return@launch
                    }
                }
                _settingsState.update { state ->
                    state.copy(
                        isDarkTheme = event.isDarkTheme
                    )
                }
            }

            is SettingsEvent.ShowDeleteDialog -> {
                _settingsState.update { state ->
                    state.copy(showDeleteDialog = true)
                }
            }

            is SettingsEvent.DismissDeleteDialog -> {
                _settingsState.update { state ->
                    state.copy(showDeleteDialog = false)
                }
            }

            is SettingsEvent.ApproveDeleteDialog -> {

                viewModelScope.launch(Dispatchers.IO) {
                    if (_settingsState.value.isDeletePaymentsPicked) {
                        try {
                            if (!_settingsState.value.isDeleteMoneySourcesPicked) {
                                updateAllMoneySourcesAmount()
                            }
                            paymentUseCases.clearAllPaymentsUseCase()
                        } catch (e: Exception) {
                            _sideEffects.emit(
                                SettingsSideEffects.ShowSnackbar(
                                    message = UiText.StringResource((R.string.error_delete_all))
                                )
                            )
                        }
                    }
                }

                viewModelScope.launch(Dispatchers.IO) {
                    if (_settingsState.value.isDeleteMoneySourcesPicked) {
                        try {
                            moneySourceUseCases.clearAllMoneySourcesUseCase()
                        } catch (e: Exception) {
                            _sideEffects.emit(
                                SettingsSideEffects.ShowSnackbar(
                                    message = UiText.StringResource((R.string.error_delete_all))
                                )
                            )
                        }
                    }
                }

                _settingsState.update { state ->
                    state.copy(showDeleteDialog = false)
                }
            }

            is SettingsEvent.ClickOnMoneySourcesCheckBox -> {
                _settingsState.update { state ->
                    state.copy(
                        isDeleteMoneySourcesPicked = !state.isDeleteMoneySourcesPicked
                    )
                }
            }

            is SettingsEvent.ClickOnPaymentsCheckBox -> {
                _settingsState.update { state ->
                    state.copy(
                        isDeletePaymentsPicked = !state.isDeletePaymentsPicked
                    )
                }
            }

            is SettingsEvent.ShowCurrencyMenu -> {
                _settingsState.update { state ->
                    state.copy(
                        isCurrenciesVisible = true
                    )
                }
            }

            is SettingsEvent.SelectedCurrency -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        settingsUseCases.changeCurrencyUseCase(newSign = event.sign)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        _sideEffects.emit(
                            SettingsSideEffects.ShowSnackbar(
                                message = UiText.StringResource(R.string.error_currency_picked)
                            )
                        )
                        return@launch
                    }
                }

                _settingsState.update { state ->
                    state.copy(
                        selectedCurrency = event.sign
                    )
                }
            }

            is SettingsEvent.DismissCurrencyMenu -> {
                _settingsState.update { state ->
                    state.copy(
                        isCurrenciesVisible = false
                    )
                }
            }
        }
    }


    private fun getInitialTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCases.getCurrentThemeUseCase().collect { isDark ->
                _settingsState.update { state ->
                    state.copy(
                        isDarkTheme = isDark,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun getInitialCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCases.getChosenCurrencyUseCase().collect { currencySign ->
                _settingsState.update { state ->
                    state.copy(selectedCurrency = currencySign)
                }
            }
        }
    }

    private suspend fun updateAllMoneySourcesAmount() {
        val (idSumMap, moneySources) = combine(
            paymentUseCases.getIncomesUseCase(),
            paymentUseCases.getExpensesUseCase(),
            moneySourceUseCases.getAllMoneySourcesUseCase()
        ) { incomes, expenses, moneySources ->
            val groupedIncomes = incomes.filter { it.sourceId != null }
                .groupBy { payment -> payment.sourceId }
                .mapNotNull { it.key to it.value.sumOf { payment -> payment.amountNew!! } }.toMap()

            val groupedExpenses = expenses.filter { it.sourceId != null }
                .groupBy { payment -> payment.sourceId }
                .mapNotNull { it.key to it.value.sumOf { payment -> payment.amountNew!! } }.toMap()

            val allKeys = (groupedIncomes.keys + groupedExpenses.keys).distinct()
            val combinedMap = allKeys.associateWith { key ->
                val incomesSum = groupedIncomes[key] ?: 0.0
                val expensesSum = groupedExpenses[key] ?: 0.0
                incomesSum - expensesSum
            }
            combinedMap to moneySources
        }.first()
        val moneySourceMap = moneySources.associateBy { it.id }

        idSumMap.entries.forEach { (sourceId, deletedAmount) ->
            val moneySource = moneySourceMap[sourceId]

            updateMoneySourceAmountOfDeletedItems(
                deletedAmount = deletedAmount,
                moneySource = moneySource
            )
        }
    }

    private suspend fun updateMoneySourceAmountOfDeletedItems(
        deletedAmount: Double?,
        moneySource: MoneySource?
    ) {
        if (moneySource == null || deletedAmount == null) return

        moneySourceUseCases.editMoneySourceUseCase(
            moneySource = moneySource.copy(
                amount = moneySource.amount - deletedAmount
            )
        )

    }

}