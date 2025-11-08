package com.example.Run_App.ui.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.Run_App.Navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(onItemSelected: (String) -> Unit) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Menu", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Quick Run",
            modifier = Modifier
                .clickable {onItemSelected("Quick Run")}
                .padding(16.dp)
        )

        Text("Guided Run",
            modifier = Modifier
                .clickable {onItemSelected("Guided Run")}
                .padding(16.dp)
        )

        Text("Settings",
            modifier = Modifier
                .clickable {onItemSelected("Settings")}
                .padding(16.dp)
        )
    }
}

@Composable
fun DrawerItem(label : String, onClick : () -> Unit) {
    Text(
        text = label,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {onClick()})
            }
    )
}

private fun navigateSafely(
    navController : NavController,
    route : String,
    drawerState : DrawerState,
    scope : kotlinx.coroutines.CoroutineScope
) {
    scope.launch {
        drawerState.close()
        val currentRoute = navController.currentDestination?.route
        if(currentRoute != route) {
            Log.d("DrawerMenu", "Navigating to $route from $currentRoute")
            navController.navigate(route) {
                popUpTo(Screen.QuickRun.route) {saveState = true}
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}