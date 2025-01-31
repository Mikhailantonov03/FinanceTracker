package com.devautro.financetracker.feature_payment.presentation.add_payment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.devautro.financetracker.core.util.Const
import com.devautro.financetracker.feature_moneySource.domain.model.MoneySource

@Composable
fun TextFieldWithDropDownMenu(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onDismissMenu: () -> Unit,
    selectedItem: String?,
    onSelectedItemChange: (String) -> Unit,
    labelText: String,
    trailingIcon: @Composable (() -> Unit),
    supportingText: @Composable (() -> Unit)? = null
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val resId = Const.getResourceIdByEnglishMonth(selectedItem ?: "")
    val selectedMonthTag = resId?.let { stringResource(id = it) }

    Column {
        TextFieldComponent(
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            value = selectedMonthTag ?: "",
            onValueChange = { /* read-only! */ },
            labelText = labelText,
            trailingIcon = trailingIcon,
            readOnly = true,
            supportingText = if (selectedMonthTag == null) supportingText else null
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onDismissMenu() },
            scrollState = rememberScrollState(),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .fillMaxHeight(0.3f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Const.months.forEach { month ->
                DropdownMenuItem(
                    onClick = {
                        onDismissMenu()
                        onSelectedItemChange(month.value)
                    },
                    text = {
                        Text(
                            text = stringResource(id = month.key),
                            fontSize = 18.sp,
                            color = if (selectedItem == month.value) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun TextFieldWithDropDownMenuMoneySource(
    modifier: Modifier = Modifier,
    itemsList: List<MoneySource>,
    isExpanded: Boolean,
    onDismissMenu: () -> Unit,
    selectedItem: MoneySource?,
    onSelectedItemChange: (MoneySource) -> Unit,
    labelText: String,
    trailingIcon: @Composable (() -> Unit),
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null
) {
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column{
        OutlinedTextField(
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .fillMaxWidth(0.8f)
                .padding(top = 10.dp),
            value = selectedItem?.name ?: "",
            onValueChange = { /* read-only! */ },
            label = { Text(text = labelText) },
            trailingIcon = trailingIcon,
            leadingIcon = if(selectedItem != null) leadingIcon else null,
            readOnly = true,singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary
            ),
            supportingText = if (selectedItem == null) supportingText else null
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onDismissMenu() },
            scrollState = rememberScrollState(),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .then(
                    if(itemsList.size < 5) Modifier.height(IntrinsicSize.Max)
                    else Modifier.fillMaxHeight(0.3f)
                )
                .background(MaterialTheme.colorScheme.background)
        ) {
            itemsList.forEach { item ->
                DropdownMenuItem(
                    modifier = if (item == selectedItem) Modifier.background(Color(item.paleColor))
                    else Modifier,
                    onClick = {
                        onDismissMenu()
                        onSelectedItemChange(item)
                    },
                    text = {
                        Text(
                            text = item.name,
                            fontSize = 18.sp,
                            color = when {
                                item == selectedItem -> MaterialTheme.colorScheme.background
                                else -> MaterialTheme.colorScheme.onBackground
                            }
                        )
                    }
                )
            }
        }
    }
}


