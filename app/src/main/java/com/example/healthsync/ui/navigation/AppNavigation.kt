package com.example.healthsync.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthsync.ui.health.HealthDataScreen
import com.example.healthsync.ui.health.HealthPermissionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "permission") {
        composable("permission") {
            HealthPermissionScreen(
                onGranted = {
                    navController.navigate("health_data") {
                        popUpTo("permission") { inclusive = true }
                    }
                },
            )
        }
        composable("health_data") {
            HealthDataScreen()
        }
    }
}
