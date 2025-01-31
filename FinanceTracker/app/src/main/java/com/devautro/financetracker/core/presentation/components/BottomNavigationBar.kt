package com.devautro.financetracker.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.devautro.financetracker.core.presentation.TabBarItem
import com.devautro.financetracker.core.presentation.util.AutoResizedText

@Composable
fun BottomNavigationBar(
    barItems:  List<TabBarItem<out Any>>,
    navController: NavController,
    currentDestination: NavDestination?
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        barItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                onClick = {
                    navController.navigate(item.route) {

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true

                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (
                            currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                        ) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.name
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedIconColor = MaterialTheme.colorScheme.onTertiary,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onTertiary,
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
                label = {
                    AutoResizedText(text = item.name)
                }
            )
        }

    }
}