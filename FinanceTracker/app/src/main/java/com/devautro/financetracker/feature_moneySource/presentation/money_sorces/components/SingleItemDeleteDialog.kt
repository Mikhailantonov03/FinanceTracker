package com.devautro.financetracker.feature_moneySource.presentation.money_sorces.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.devautro.financetracker.R

@Composable
fun SingleItemDeleteDialog(
    onDismissDialog: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissDialog
    ) {
        Card(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Min)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.one_time_dialog_1))
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.one_time_dialog_2))
                    }
                    append(stringResource(id = R.string.one_time_dialog_3))
                },
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.one_time_dialog_4))
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.one_time_dialog_5))
                    }
                    append(stringResource(id = R.string.one_time_dialog_6))
                },
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}