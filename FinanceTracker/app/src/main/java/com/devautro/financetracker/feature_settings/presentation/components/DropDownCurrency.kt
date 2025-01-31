package com.devautro.financetracker.feature_settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import com.devautro.financetracker.R

@Composable
fun DropDownCurrency(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onDismissMenu: () -> Unit,
    selectedItem: String,
    onSelectedItemIdChange: (String) -> Unit,
    offsetX: DpOffset
) {
    val none = stringResource(id = R.string.none)

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onDismissMenu() },
        scrollState = rememberScrollState(),
        modifier = modifier
            .fillMaxHeight(0.3f)
            .background(MaterialTheme.colorScheme.background),
        offset = offsetX
    ) {
        listOf(
            stringResource(id = R.string.none),
            stringResource(id = R.string.colon),
            stringResource(id = R.string.dollar),
            stringResource(id = R.string.dram),
            stringResource(id = R.string.euro),
            stringResource(id = R.string.frank),
            stringResource(id = R.string.guarani),
            stringResource(id = R.string.grivna),
            stringResource(id = R.string.lari),
            stringResource(id = R.string.lira),
            stringResource(id = R.string.manat),
            stringResource(id = R.string.naira),
            stringResource(id = R.string.peso),
            stringResource(id = R.string.pound),
            stringResource(id = R.string.ruble),
            stringResource(id = R.string.rupee),
            stringResource(id = R.string.shekel),

            stringResource(id = R.string.tenge),
            stringResource(id = R.string.tugrik),
            stringResource(id = R.string.won),
            stringResource(id = R.string.yen)
        ).forEach { sign ->
            DropdownMenuItem(
                onClick = {
                    onDismissMenu()
                    if (sign == none) {
                        onSelectedItemIdChange("")
                    } else {
                        onSelectedItemIdChange(sign)
                    }
                },
                text = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = sign,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = when {
                            sign == selectedItem -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.onBackground
                        }
                    )
                }
            )
        }
    }
}