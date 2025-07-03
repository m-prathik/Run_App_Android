package com.example.Run_App.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Run_App.login.LoginScreen
import com.example.Run_App.ui.QuickGuidedRunScreen
import com.example.Run_App.ui.QuickRunScreen
import com.example.Run_App.ui.QuickSettingsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.QuickRun.route) {
            QuickRunScreen(navController)
        }
        composable(route = Screen.GuidedRun.route) {
            QuickGuidedRunScreen(navController)
        }

        composable(route = Screen.SettingScreen.route) {
            QuickSettingsScreen(navController)
        }
    }
}