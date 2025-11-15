package com.zetta.tiksid.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.zetta.tiksid.R

sealed class BottomNavItem(
    val label: String,
    val route: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int = selectedIcon
) {
    object Home : BottomNavItem(
        label = "Home",
        route = Screen.Home.route,
        selectedIcon = R.drawable.home_filled,
        unselectedIcon = R.drawable.home
    )
    object Browse : BottomNavItem(
        label = "Browse",
        route = Screen.Browse.route,
        selectedIcon = R.drawable.category_filled,
        unselectedIcon = R.drawable.category
    )
    object Tickets : BottomNavItem(
        label = "Your Ticket",
        route = Screen.TicketList.route,
        selectedIcon = R.drawable.ticket_filled,
        unselectedIcon = R.drawable.ticket
    )
}

@Composable
fun BottomNavBar(
    onNavigate: (String) -> Unit,
    currentDestination: NavDestination
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Browse,
        BottomNavItem.Tickets
    )

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                .defaultMinSize(minHeight = 64.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.map { item ->
                val selected = currentDestination.route == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (selected) item.selectedIcon
                                    else item.unselectedIcon
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                        unselectedTextColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        }
    }
}