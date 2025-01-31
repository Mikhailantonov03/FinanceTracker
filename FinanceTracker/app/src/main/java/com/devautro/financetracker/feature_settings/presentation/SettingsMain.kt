package com.devautro.financetracker.feature_settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GTranslate
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devautro.financetracker.R
import com.devautro.financetracker.feature_settings.presentation.components.DeleteAllDialog
import com.devautro.financetracker.feature_settings.presentation.components.DropDownCurrency
import com.devautro.financetracker.feature_settings.presentation.components.DropDownLanguageMenu
import com.devautro.financetracker.feature_settings.presentation.components.SettingsItem
import com.devautro.financetracker.ui.theme.CancelButton
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMain(
    viewModel: SettingsViewModel = hiltViewModel(),
    bottomPadding: PaddingValues
) {
    val state by viewModel.settingsState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is SettingsSideEffects.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context)
                    )
                }
            }
        }
    }

    val density = LocalDensity.current
    var offsetX by remember {
        mutableStateOf(0.dp)
    }

    var parentWidth by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_header),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { topPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding()
                )
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                    ) {
                        SettingsItem(
                            modifier = Modifier.clickable {
                                viewModel.onEvent(SettingsEvent.ShowLanguageMenu)
                            },
                            headerText = stringResource(id = R.string.language),
                            bodyText = stringResource(id = R.string.choose_language_text),
                            icon = Icons.Filled.GTranslate,
                            contentDescription = stringResource(id = R.string.icon_language_description),
                            switcher = {
                                Icon(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(15.dp)),
                                    painter = painterResource(id = state.selectedLanguageImageId),
                                    contentDescription = stringResource(id = R.string.icon_flag_description),
                                    tint = Color.Unspecified // for correct drawable color
                                )
                            }
                        )
                        DropDownLanguageMenu(
                            modifier = Modifier.fillMaxWidth(),
                            isExpanded = state.isLanguageMenuShown,
                            onDismissMenu = { viewModel.onEvent(SettingsEvent.DismissLanguageMenu) },
                            selectedItem = state.selectedLanguageImageId,
                            onSelectedItemIdChange = { flagItemId, locale ->
                                viewModel.onEvent(
                                    SettingsEvent.NewLanguageSelected(
                                        flagImage = flagItemId,
                                        locale = locale
                                    )
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    SettingsItem(
                        headerText = stringResource(id = R.string.theme),
                        bodyText = stringResource(id = R.string.theme_body),
                        icon = Icons.Default.NightsStay,
                        contentDescription = stringResource(id = R.string.icon_theme_description),
                        switcher = {
                            Switch(
                                checked = state.isDarkTheme,
                                onCheckedChange = { isDarkTheme ->
                                    viewModel.onEvent(SettingsEvent.SwitchTheme(isDarkTheme))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.onBackground,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.secondary,
                                    uncheckedTrackColor = CancelButton
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    SettingsItem(
                        modifier = Modifier.clickable {
                            viewModel.onEvent(SettingsEvent.ShowDeleteDialog)
                        },
                        headerText = stringResource(id = R.string.delete_settings),
                        bodyText = stringResource(id = R.string.delete_settings_body),
                        icon = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.icon_delete_all_description)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Column(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                            .onPlaced {
                                parentWidth = it.size.width
                            }
                    ) {
                        SettingsItem(
                            modifier = Modifier.clickable {
                                viewModel.onEvent(SettingsEvent.ShowCurrencyMenu)
                            },
                            headerText = stringResource(id = R.string.currency),
                            bodyText = stringResource(id = R.string.currency_body),
                            icon = Icons.Filled.MonetizationOn,
                            contentDescription = stringResource(id = R.string.icon_currency_description),
                            switcher = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    text = state.selectedCurrency
                                )
                                DropDownCurrency(
                                    modifier = Modifier.onPlaced {
                                        val popUpWidthPx =
                                            parentWidth  - it.size.width

                                        offsetX = with(density) {
                                            popUpWidthPx.toDp()
                                        }

                                    },
                                    isExpanded = state.isCurrenciesVisible,
                                    onDismissMenu = { viewModel.onEvent(SettingsEvent.DismissCurrencyMenu) },
                                    selectedItem = state.selectedCurrency,
                                    onSelectedItemIdChange = { sign ->
                                        viewModel.onEvent(SettingsEvent.SelectedCurrency(sign))
                                    },
                                    offsetX = DpOffset(offsetX, 0.dp)
                                )
                            }
                        )

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (state.showDeleteDialog) {
                DeleteAllDialog(
                    onConfirmClick = { viewModel.onEvent(SettingsEvent.ApproveDeleteDialog) },
                    onDismissClick = { viewModel.onEvent(SettingsEvent.DismissDeleteDialog) },
                    isDeleteMoneySources = state.isDeleteMoneySourcesPicked,
                    onMsCheckedChange = { viewModel.onEvent(SettingsEvent.ClickOnMoneySourcesCheckBox) },
                    isDeletePayments = state.isDeletePaymentsPicked,
                    onPaymentsCheckedChange = { viewModel.onEvent(SettingsEvent.ClickOnPaymentsCheckBox) }
                )
            }

        }
    }
}