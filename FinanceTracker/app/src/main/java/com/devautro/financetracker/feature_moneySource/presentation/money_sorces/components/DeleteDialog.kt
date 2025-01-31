package com.devautro.financetracker.feature_moneySource.presentation.money_sorces.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.devautro.financetracker.R

@Composable
fun DeleteDialog(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(
                    text = stringResource(id = R.string.delete_text),
                    color = MaterialTheme.colorScheme.errorContainer
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissClick) {
                Text(text = stringResource(id = R.string.cancel), color = MaterialTheme.colorScheme.primary)
            }
        },
        title = {
            Text(text = stringResource(id = R.string.delete_text))
        },
        text = {
            Text(
                buildAnnotatedString {
                    append(stringResource(id = R.string.delete_dialog_text_1))

                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.errorContainer)) {
                        append(stringResource(id = R.string.delete_dialog_text_2))
                    }
                    append(stringResource(id = R.string.delete_dialog_text_3))
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.delete_dialog_text_4))
                    }
                    append(stringResource(id = R.string.delete_dialog_text_5))
                },
                fontSize = 16.sp
            )
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        textContentColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.background
    )
}