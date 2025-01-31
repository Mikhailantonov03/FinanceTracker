package com.devautro.financetracker.feature_payment.presentation.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devautro.financetracker.R
import com.devautro.financetracker.core.presentation.components.TopTabsSorting
import com.devautro.financetracker.core.util.Const
import com.devautro.financetracker.feature_payment.presentation.home_screen.components.InfoCard
import com.devautro.financetracker.ui.theme.BackgroundColor
import com.devautro.financetracker.ui.theme.DarkGreenCircle
import com.devautro.financetracker.ui.theme.DarkRedCircle
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    bottomPadding: PaddingValues,
    navigateToIncomes: () -> Unit,
    navigateToExpenses: () -> Unit,
    currencySign: String
) {
    val state by viewModel.homeState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.sideEffects.collectLatest { effect ->
            when(effect) {
                is HomeScreenSideEffects.IncomesClick -> navigateToIncomes()

                is HomeScreenSideEffects.ExpensesClick -> navigateToExpenses()

                is HomeScreenSideEffects.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home_topbar_text),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding()
                )
                .background(MaterialTheme.colorScheme.background),
        ) {
            TopTabsSorting(
                tabItems = Const.filterTags.map { stringResource(id = it) },
                selectedTabIndex = state.selectedTabIndex,
                onSelectedTabClick = { tabIndex ->
                    viewModel.onEvent(HomeScreenEvent.TabClick(tabIndex = tabIndex))
                }
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()

            ) {
                item {
                    InfoCard(
                        modifier = Modifier.clickable {
                            viewModel.onEvent(HomeScreenEvent.IncomesClick)
                        },
                        text = stringResource(id = R.string.incomes),
                        amount = "$currencySign${state.incomesSum}",
                        color = DarkGreenCircle,
                    )
                }
                item {
                    InfoCard(
                        modifier = Modifier.clickable {
                            viewModel.onEvent(HomeScreenEvent.ExpensesClick)
                        },
                        text = stringResource(id = R.string.expenses),
                        amount = "$currencySign${state.expensesSum}",
                        color = DarkRedCircle
                    )
                }
                item {
                    InfoCard(
                        text = stringResource(id = R.string.budget),
                        amount = "$currencySign${state.budgetSum}",
                        color = BackgroundColor
                    )
                }
            }
        }

    }
}