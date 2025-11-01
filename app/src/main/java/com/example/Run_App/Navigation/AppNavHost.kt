package com.example.Run_App.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Run_App.login.LoginScreen
import com.example.Run_App.ui.QuickGuidedRunScreen
import com.example.Run_App.ui.run.quickRunScreen
import com.example.Run_App.ui.QuickSettingsScreen
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun Navigation(fusedLocationProviderClient: FusedLocationProviderClient) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.QuickRun.route) {
            quickRunScreen(
                fusedLocationClient = fusedLocationProviderClient,
                mapboxToken = "pk.eyJ1IjoicHJhdGhpay1kZCIsImEiOiJjbWd1a3RrNzcwZzRnMmpyN2tpM2owdnZuIn0.ToCguqSzxFuHYoqsB3tv7w",
                navController = navController)
        }
        composable(route = Screen.GuidedRun.route) {
            QuickGuidedRunScreen(navController)
        }

        composable(route = Screen.SettingScreen.route) {
            QuickSettingsScreen(navController)
        }
    }
}