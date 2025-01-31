package com.devautro.financetracker.feature_payment.presentation.payments_type_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devautro.financetracker.core.util.Const

@Composable
fun SelectedMonthContainer(
    modifier: Modifier = Modifier,
    monthTag: String
) {
    val resId = Const.getResourceIdByEnglishMonth(monthTag)
    val month = resId?.let { stringResource(id = it) } ?: monthTag

    Card(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = month,
                modifier = Modifier.padding(5.dp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}