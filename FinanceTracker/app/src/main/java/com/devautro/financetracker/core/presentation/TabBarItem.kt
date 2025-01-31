package com.devautro.financetracker.core.presentation

import androidx.compose.ui.graphics.vector.ImageVector

data class TabBarItem<T : Any>(
    val name: String,
    val route: T,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

