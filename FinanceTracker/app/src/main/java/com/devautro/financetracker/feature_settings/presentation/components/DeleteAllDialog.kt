package com.devautro.financetracker.feature_settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.devautro.financetracker.R

@Composable
fun DeleteAllDialog(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    isDeleteMoneySources: Boolean,
    onMsCheckedChange: () -> Unit,
    isDeletePayments: Boolean,
    onPaymentsCheckedChange: () -> Unit,
) {
    val isEnabled = isDeletePayments || isDeleteMoneySources

    AlertDialog(
        onDismissRequest = onDismissClick,
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                enabled = isEnabled
            ) {
                Text(
                    text = stringResource(id = R.string.delete_text),
                    color = if (isEnabled) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissClick) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(text = stringResource(id = R.string.delete_text))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDeleteMoneySources,
                        onCheckedChange = {
                            onMsCheckedChange()
                        },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = MaterialTheme.colorScheme.secondary,
                            checkedColor = MaterialTheme.colorScheme.background,
                            uncheckedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(id = R.string.delete_mss1))

                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append(stringResource(id = R.string.delete_mss2))
                            }
                        },
                        fontSize = 16.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDeletePayments,
                        onCheckedChange = {
                            onPaymentsCheckedChange()
                        },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = MaterialTheme.colorScheme.secondary,
                            checkedColor = MaterialTheme.colorScheme.background,
                            uncheckedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(id = R.string.delete_payments1))

                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append(stringResource(id = R.string.delete_payments2))
                            }
                        },
                        fontSize = 16.sp
                    )
                }

            }
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        textContentColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.background
    )
}