package com.example.Run_App.ui.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.Run_App.Navigation.Screen
import com.example.Run_App.ui.run.quickRun.quickRunScreen
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.Run_App.BuildConfig
import com.example.Run_App.ui.QuickGuidedRunScreen
import com.example.Run_App.ui.QuickSettingsScreen


@Composable
fun MainScreen(navController: NavHostController, fusedLocationProviderClient: FusedLocationProviderClient) {
    Log.d("prathik", "main screen recomposed")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf("Quick Run") }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(onItemSelected = {
                screenName ->
                selectedScreen = screenName
                scope.launch { drawerState.close() }
            }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(selectedScreen)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
        ) {paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                ) {
                    when(selectedScreen) {
                        "Quick Run" -> quickRunScreen(
                            navController = navController,
                            mapboxToken = BuildConfig.MAPBOX_TOKEN
                        )
                        "Guided Run" -> QuickGuidedRunScreen(
                            navController = navController
                        )
                        "Settings" -> QuickSettingsScreen(
                            navController = navController
                        )
                    }
                }

        }
    }
}