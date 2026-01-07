package com.blazon.app.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object BranchSelection : Screen("branch_selection")
    object Home : Screen("home")
    object Services : Screen("services")
    object Membership : Screen("membership")
    object VisitTracking : Screen("visit_tracking")
    object Profile : Screen("profile")
}

