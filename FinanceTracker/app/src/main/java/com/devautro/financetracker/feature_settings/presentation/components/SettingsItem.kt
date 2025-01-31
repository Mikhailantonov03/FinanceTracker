package com.devautro.financetracker.feature_settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.devautro.financetracker.core.presentation.components.ActionIcon
import com.devautro.financetracker.core.presentation.util.AutoResizedText

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    headerText: String,
    bodyText: String,
    icon: ImageVector,
    contentDescription: String? = null,
    switcher: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionIcon(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp)),
                onClick = { /*TODO*/ },
                backgroundColor = MaterialTheme.colorScheme.secondary,
                icon = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.background,
                isEnabled = false,
                disabledTint = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalArrangement = Arrangement.Center,
            ) {
                AutoResizedText(
                    text = headerText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                AutoResizedText(
                    text = bodyText,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (switcher != null) switcher()
        }
    }
}