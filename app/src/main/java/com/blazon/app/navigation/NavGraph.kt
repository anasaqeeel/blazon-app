package com.blazon.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.blazon.app.ui.screens.*

@Composable
fun BlazonNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    var selectedBranchId by remember { mutableStateOf<String?>(null) }
    val userId = "user-1"
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToNext = {
                        if (selectedBranchId != null) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.BranchSelection.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
            
            composable(Screen.BranchSelection.route) {
                BranchSelectionScreen(
                    onBranchSelected = { branchId ->
                        selectedBranchId = branchId
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.BranchSelection.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Home.route) {
                val branchId = selectedBranchId ?: return@composable
                
                HomeScreen(
                    branchId = branchId,
                    onNavigateToServices = {
                        navController.navigate(Screen.Services.route)
                    },
                    onNavigateToMembership = {
                        navController.navigate(Screen.Membership.route)
                    },
                    onNavigateToAvailability = {
                        // TODO: Navigate to availability screen if needed
                    },
                    onNavigateToVisitTracking = {
                        navController.navigate(Screen.VisitTracking.route)
                    }
                )
            }
            
            composable(Screen.Services.route) {
                val branchId = selectedBranchId ?: return@composable
                
                ServicesScreen(branchId = branchId)
            }
            
            composable(Screen.Membership.route) {
                val branchId = selectedBranchId ?: return@composable
                
                MembershipScreen(
                    branchId = branchId,
                    userId = userId
                )
            }
            
            composable(Screen.VisitTracking.route) {
                val branchId = selectedBranchId ?: return@composable
                
                VisitTrackingScreen(
                    branchId = branchId,
                    userId = userId
                )
            }
            
            composable(Screen.Profile.route) {
                val branchId = selectedBranchId ?: return@composable
                
                ProfileScreen(
                    branchId = branchId,
                    userId = userId,
                    onChangeBranch = {
                        selectedBranchId = null
                        navController.navigate(Screen.BranchSelection.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
        
        // Bottom Navigation Bar
        if (currentRoute != null && currentRoute != Screen.Splash.route && 
            currentRoute != Screen.BranchSelection.route) {
            BlazonBottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

