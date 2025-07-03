package com.example.Run_App.Navigation

sealed class Screen(val route: String) {
    object Home : Screen("login")
    object QuickRun : Screen("QuickRunScreen")
    object GuidedRun : Screen("QuickGuidedRunScreen")
    object SettingScreen : Screen("QuickSettingsScreen")
}