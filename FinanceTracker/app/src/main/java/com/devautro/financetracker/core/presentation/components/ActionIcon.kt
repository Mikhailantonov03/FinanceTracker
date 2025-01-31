package com.devautro.financetracker.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ActionIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    tint: Color = Color.White,
    disabledTint: Color = Color.DarkGray,
    contentDescription: String? = null,
    isEnabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(backgroundColor),
        enabled = isEnabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isEnabled) tint else disabledTint
        )
    }

}