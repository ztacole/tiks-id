package com.zetta.tiksid.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.zetta.tiksid.R
import com.zetta.tiksid.navigation.Screen
import com.zetta.tiksid.ui.theme.AppTheme
import com.zetta.tiksid.utils.Constants

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
        label = "Your Tickets",
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
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                .defaultMinSize(minHeight = Constants.NAVIGATION_BAR_HEIGHT)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.map { item ->
                val selected = currentDestination.route == item.route
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = Constants.NAVIGATION_BAR_HEIGHT)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(radius = Constants.NAVIGATION_BAR_HEIGHT)
                        ) { onNavigate(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(
                            if (selected) item.selectedIcon
                            else item.unselectedIcon
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary
                    )
                    AnimatedVisibility(selected) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Prev() {
    AppTheme {
        Surface {
            BottomNavBar({}, NavDestination(""))
        }
    }
}