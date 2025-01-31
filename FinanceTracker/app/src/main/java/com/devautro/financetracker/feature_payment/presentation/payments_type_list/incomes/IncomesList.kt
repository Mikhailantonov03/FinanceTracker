package com.devautro.financetracker.feature_payment.presentation.payments_type_list.incomes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devautro.financetracker.R
import com.devautro.financetracker.core.presentation.components.ActionIcon
import com.devautro.financetracker.feature_payment.presentation.edit_payment.EditPaymentSheet
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.components.MonthTagsDrawerMenu
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.components.PaymentTypeCard
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.components.SelectedMonthContainer
import com.devautro.financetracker.core.presentation.components.SwipeableItem
import com.devautro.financetracker.core.util.Const
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.PaymentsListEvent
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.PaymentsListSideEffects
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.components.TotalAmountCard
import com.devautro.financetracker.feature_payment.presentation.payments_type_list.components.YearPicker
import com.devautro.financetracker.feature_payment.util.convertMillisToDate
import com.devautro.financetracker.core.util.formatDoubleToString
import com.devautro.financetracker.ui.theme.DarkestColor
import com.devautro.financetracker.ui.theme.IncomeGreen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesList(
    viewModel: IncomesViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navBarPadding: PaddingValues,
    currencySign: String
) {
    val state by viewModel.paymentsState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val lazyList = rememberLazyListState()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is PaymentsListSideEffects.NavigateBack -> navigateBack()
                is PaymentsListSideEffects.ShowSnackBar -> {
                    if (effect.snackbarButtonText != null) {
                        snackbarHostState.showSnackbar(
                            message = effect.message.asString(context),
                            actionLabel = effect.snackbarButtonText.asString(context),
                            duration = SnackbarDuration.Short
                        ).let { res ->
                            if (res == SnackbarResult.ActionPerformed) {
                                viewModel.onEvent(PaymentsListEvent.RestorePayment)
                            }
                        }
                    } else {
                        snackbarHostState.showSnackbar(
                            message = effect.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = state.paymentItemsList.size) {
        if (state.paymentItemsList.isNotEmpty()) lazyList.scrollToItem(0)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (state.selectedMonthTag.isEmpty() || state.selectedMonthTag.isBlank()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = stringResource(id = R.string.incomes))
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SelectedMonthContainer(monthTag = state.selectedMonthTag)
                            AnimatedVisibility(visible = state.paymentYearsList.isNotEmpty()) {
                                YearPicker(
                                    yearsList = state.paymentYearsList,
                                    yearIndex = state.selectedYearIndex,
                                    onYearChanged = { newIndex ->
                                        viewModel.onEvent(
                                            PaymentsListEvent.CurrentYearSelected(
                                                yearIndex = newIndex
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(PaymentsListEvent.NavigateBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.icon_arrow_back_description),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        if (state.selectedMonthTag.isNotBlank()) {
                            IconButton(onClick = {
                                viewModel.onEvent(PaymentsListEvent.ClearIconCLick)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = stringResource(id = R.string.icon_clear_filter_description),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        IconButton(onClick = {
                            viewModel.onEvent(PaymentsListEvent.FilterIconClick)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.FilterAlt,
                                contentDescription = stringResource(id = R.string.icon_filter_payments_description),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        MonthTagsDrawerMenu(
                            isExpanded = state.isMonthTagMenuVisible,
                            onDismissMenu = {
                                viewModel.onEvent(PaymentsListEvent.DismissMonthTagMenu)
                            },
                            selectedItem = state.selectedMonthTag,
                            onSelectedItem = { month ->
                                viewModel.onEvent(
                                    PaymentsListEvent.MonthTagSelected(
                                        monthTag = month
                                    )
                                )
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = navBarPadding.calculateBottomPadding()
                )
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TotalAmountCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                amount = "$currencySign${state.totalAmount}"
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = lazyList,
                reverseLayout = true
            ) {
                itemsIndexed(
                    items = state.paymentItemsList,
                    key = { index, paymentItem -> paymentItem.id ?: index }
                ) { _, payment ->
                    SwipeableItem(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 16.dp),
                        isRevealed = payment.isRevealed,
                        actions = {
                            ActionIcon(
                                onClick = {
                                    viewModel.onEvent(
                                        PaymentsListEvent.ItemRevealed(
                                            id = payment.id!!,
                                            isRevealed = false
                                        )
                                    )
                                    viewModel.onEvent(
                                        PaymentsListEvent.EditIconClick(
                                            paymentItem = payment
                                        )
                                    )
                                },
                                backgroundColor = DarkestColor,
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                icon = Icons.Default.Edit,
                                contentDescription = stringResource(id = R.string.icon_edit_payment_description),
                                modifier = Modifier.clip(RoundedCornerShape(15.dp))
                            )
                            ActionIcon(
                                onClick = {
                                    viewModel.onEvent(
                                        PaymentsListEvent.ItemRevealed(
                                            id = payment.id!!,
                                            isRevealed = false
                                        )
                                    )
                                    viewModel.onEvent(
                                        PaymentsListEvent.DeleteIconClick(
                                            paymentItem = payment
                                        )
                                    )
                                },
                                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                icon = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.icon_delete_payment_description),
                                modifier = Modifier.clip(RoundedCornerShape(15.dp))
                            )
                        },
                        onExpanded = {
                            viewModel.onEvent(
                                PaymentsListEvent.ItemRevealed(
                                    id = payment.id!!,
                                    isRevealed = true
                                )
                            )
                        },
                        onCollapsed = {
                            viewModel.onEvent(
                                PaymentsListEvent.ItemRevealed(
                                    id = payment.id!!,
                                    isRevealed = false
                                )
                            )
                        }
                    ) {
                        val resId = Const.getResourceIdByEnglishMonth(payment.monthTag)
                        val monthTag = resId?.let { stringResource(id = it) } ?: payment.monthTag

                        PaymentTypeCard(
                            description = payment.description,
                            amount = formatDoubleToString(
                                value = payment.amountNew,
                                sign = currencySign
                            ),
                            monthTag = monthTag,
                            date = convertMillisToDate(payment.date),
                            color = IncomeGreen
                        )
                    }
                }
            }
            if (state.isEditBottomSheetVisible) {
                EditPaymentSheet(
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    navigateBack = { viewModel.onEvent(PaymentsListEvent.ShowEditBottomSheet) },
                    initialPayment = state.paymentForSheet
                )
            }
        }
    }
}