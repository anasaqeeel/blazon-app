package com.blazon.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blazon.app.theme.BlazonCard
import com.blazon.app.theme.BlazonForeground
import com.blazon.app.theme.BlazonGold
import com.blazon.app.theme.BlazonMutedForeground

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Outlined.Home)
    object Services : BottomNavItem(Screen.Services.route, "Services", Icons.Outlined.ContentCut)
    object Rewards : BottomNavItem(Screen.Rewards.route, "Rewards", Icons.Outlined.Stars)
    object Scan : BottomNavItem(Screen.VisitTracking.route, "Scan", Icons.Outlined.QrCodeScanner)
    object Settings : BottomNavItem(Screen.Profile.route, "Settings", Icons.Outlined.Settings)
}

@Composable
fun BlazonBottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Services,
        BottomNavItem.Rewards,
        BottomNavItem.Scan,
        BottomNavItem.Settings
    )

    NavigationBar(
        modifier = modifier,
        containerColor = BlazonCard,
        contentColor = BlazonForeground,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = BlazonForeground,
                    selectedTextColor = BlazonGold,
                    indicatorColor = BlazonGold.copy(alpha = 0.25f),
                    unselectedIconColor = BlazonMutedForeground,
                    unselectedTextColor = BlazonMutedForeground
                )
            )
        }
    }
}
