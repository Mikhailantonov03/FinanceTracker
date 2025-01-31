package com.devautro.financetracker.feature_moneySource.presentation.money_sorces

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.SwitchLeft
import androidx.compose.material.icons.filled.SwitchRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devautro.financetracker.R
import com.devautro.financetracker.core.presentation.components.ActionIcon
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.components.DeleteDialog
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.components.MoneySourceCard
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.components.TableDataItem
import com.devautro.financetracker.core.presentation.components.SwipeableItem
import com.devautro.financetracker.feature_moneySource.presentation.money_sorces.components.SingleItemDeleteDialog
import com.devautro.financetracker.core.util.formatDoubleToString
import com.devautro.financetracker.feature_statistics.util.formatNumber
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneySources(
    viewModel: MoneySourcesViewModel = hiltViewModel(),
    bottomNavPadding: PaddingValues,
    navigateToAddScreen: () -> Unit,
    navigateToEditScreen: (Long, Int) -> Unit,
    currencySign: String
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.moneySourcesState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is MoneySourcesSideEffects.NavigateAddScreen -> {
                    navigateToAddScreen()
                }

                is MoneySourcesSideEffects.NavigateEditScreen -> {
                    navigateToEditScreen(effect.id, effect.paleColor)
                }

                is MoneySourcesSideEffects.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.money_sources),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    ActionIcon(
                        onClick = {
                            viewModel.onEvent(MoneySourcesEvent.AddIconClick)
                        },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        icon = Icons.Filled.AddCircleOutline,
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(RoundedCornerShape(15.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    ActionIcon(
                        modifier = Modifier
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp)),
                        onClick = {
                            viewModel.onEvent(MoneySourcesEvent.FilterIncludedClick)
                        },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        icon = if (state.isIncludedOnlyFilter) Icons.Default.FilterAlt else Icons.Default.FilterAltOff,
                        tint = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    ActionIcon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp)),
                        onClick = {
                            viewModel.onEvent(MoneySourcesEvent.SwitcherIconClick)
                        },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        tint = MaterialTheme.colorScheme.background,
                        icon = if (state.isTableFormatData || isLandscape) {
                            Icons.Filled.SwitchLeft
                        } else Icons.Filled.SwitchRight,
                        isEnabled = !isLandscape
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            )
        },

        ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = bottomNavPadding.calculateBottomPadding()
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                        text = if (state.isIncludedOnlyFilter) {
                            stringResource(id = R.string.total_included)
                        }
                        else {
                            stringResource(id = R.string.total_money)
                        },
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(start = 20.dp, end = 10.dp),
                        text = "$currencySign${state.totalAmount}",
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = rememberLazyListState()
            ) {
                itemsIndexed(
                    items = state.moneySourceList,
                    key = { index, item -> item.id ?: index },
                ) { index, moneySourceItem ->
                    SwipeableItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        isRevealed = moneySourceItem.isRevealed,
                        actions = {
                            ActionIcon(
                                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                                onClick = {
                                    viewModel.onEvent(
                                        MoneySourcesEvent.DeleteIconClick(
                                            deleteSource = moneySourceItem
                                        )
                                    )
                                    viewModel.onEvent(
                                        MoneySourcesEvent.ItemRevealed(
                                            id = moneySourceItem.id!!,
                                            isRevealed = false
                                        )
                                    )
                                },
                                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                                icon = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.delete_ms_description),
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        },
                        onExpanded = {
                            viewModel.onEvent(
                                MoneySourcesEvent.ItemRevealed(
                                    isRevealed = true,
                                    id = moneySourceItem.id!!
                                )
                            )
                        },
                        onCollapsed = {
                            viewModel.onEvent(
                                MoneySourcesEvent.ItemRevealed(
                                    isRevealed = false,
                                    id = moneySourceItem.id!!
                                )
                            )
                        }
                    ) {
                        if (state.isTableFormatData || isLandscape) {
                            val amount = if (moneySourceItem.amount.toString().length > 7) {
                                formatNumber(moneySourceItem.amount, currencySign)
                            } else {
                                formatDoubleToString(
                                    value = moneySourceItem.amount,
                                    sign = currencySign
                                )
                            }

                            TableDataItem(
                                index = index,
                                sourceName = moneySourceItem.name,
                                amount = amount,
                                onEditClick = {
                                    viewModel.onEvent(
                                        MoneySourcesEvent.EditIconClick(
                                            id = moneySourceItem.id!!,
                                            paleColor = moneySourceItem.paleColor
                                        )
                                    )
                                },
                                backgroundColor = Color(moneySourceItem.paleColor)
                            )
                        } else {
                            MoneySourceCard(
                                modifier = Modifier
                                    .height(220.dp)
                                    .padding(vertical = 16.dp),
                                cardPaleColor = Color(moneySourceItem.paleColor),
                                cardAccentColor = Color(moneySourceItem.accentColor),
                                index = index,
                                sourceName = moneySourceItem.name,
                                amount = formatDoubleToString(
                                    value = moneySourceItem.amount,
                                    sign = currencySign
                                ),
                                onEditIconClick = {
                                    viewModel.onEvent(
                                        MoneySourcesEvent.EditIconClick(
                                            id = moneySourceItem.id!!,
                                            paleColor = moneySourceItem.paleColor
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

            }
            if (state.showDeleteDialog) {
                if (state.moneySourceList.size == 1) {
                    SingleItemDeleteDialog(
                        onDismissDialog = {
                            viewModel.onEvent(MoneySourcesEvent.DismissDeleteDialog)
                        }
                    )
                } else {
                    DeleteDialog(
                        onConfirmClick = {
                            viewModel.onEvent(MoneySourcesEvent.DeleteApproval)
                        },
                        onDismissClick = {
                            viewModel.onEvent(MoneySourcesEvent.DismissDeleteDialog)
                        }
                    )
                }
            }
        }
    }
}