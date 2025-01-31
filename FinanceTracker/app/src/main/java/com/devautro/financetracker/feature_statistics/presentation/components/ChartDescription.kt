package com.devautro.financetracker.feature_statistics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.devautro.financetracker.R
import com.devautro.financetracker.ui.theme.DarkestColor
import com.devautro.financetracker.ui.theme.ExpenseRed
import com.devautro.financetracker.ui.theme.IncomeGreen

@Composable
fun ChartDescription(
    modifier: Modifier = Modifier,
    incomesSum: String,
    expensesSum: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(IntrinsicSize.Min)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(IncomeGreen)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.incomes))
                    append(": ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(incomesSum)
                    }
                },
                color = DarkestColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(ExpenseRed)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.expenses))
                    append(": ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(expensesSum)
                    }
                },
                color = DarkestColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}