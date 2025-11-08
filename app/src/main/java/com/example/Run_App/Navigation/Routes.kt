package com.example.Run_App.Navigation

sealed class Screen(val route: String) {
    object Home : Screen("login")
    object QuickRun : Screen("quickRunScreen")
    object GuidedRun : Screen("QuickGuidedRunScreen")
    object SettingScreen : Screen("QuickSettingsScreen")
    object Main : Screen("main")
}