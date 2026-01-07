package com.blazon.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val icon: String
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", "🏠")
    object Services : BottomNavItem(Screen.Services.route, "Services", "📋")
    object Scan : BottomNavItem(Screen.VisitTracking.route, "Scan", "📱")
    object Settings : BottomNavItem(Screen.Profile.route, "Settings", "⚙️")
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
        BottomNavItem.Scan,
        BottomNavItem.Settings
    )
    
    NavigationBar(
        modifier = modifier,
        containerColor = BlazonCard,
        contentColor = BlazonForeground,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Text(
                        text = item.icon,
                        fontSize = 24.sp
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = BlazonGold,
                    selectedTextColor = BlazonGold,
                    indicatorColor = BlazonGold.copy(alpha = 0.15f),
                    unselectedIconColor = BlazonMutedForeground.copy(alpha = 0.85f),
                    unselectedTextColor = BlazonMutedForeground.copy(alpha = 0.9f)
                )
            )
        }
    }
}

