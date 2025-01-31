package com.devautro.financetracker.feature_payment.presentation.payments_type_list.components

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
import com.devautro.financetracker.core.util.Const

@Composable
fun MonthTagsDrawerMenu(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onDismissMenu: () -> Unit,
    selectedItem: String,
    onSelectedItem: (String) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = {
            onDismissMenu()
        },
        scrollState = rememberScrollState(),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Const.months.forEach { month ->

            DropdownMenuItem(
                onClick = {
                    onDismissMenu()
                    onSelectedItem(month.value)
                },
                text = {
                    Text(
                        text = stringResource(id = month.key),
                        color = if (selectedItem == month.value) {
                            MaterialTheme.colorScheme.secondary
                        } else MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }

    }
}