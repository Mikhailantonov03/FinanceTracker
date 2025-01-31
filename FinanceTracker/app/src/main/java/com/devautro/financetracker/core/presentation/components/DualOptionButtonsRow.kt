package com.devautro.financetracker.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DualOptionButtonsRow(
    dismissText: String,
    approveText: String,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    isApproveEnabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.weight(0.5f),
            onClick = {
                onDismiss()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            Text(text = dismissText)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            modifier = Modifier.weight(0.5f),
            enabled = isApproveEnabled,
            onClick = {
                onApprove()
//                onDismiss()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = approveText)
        }
    }
}