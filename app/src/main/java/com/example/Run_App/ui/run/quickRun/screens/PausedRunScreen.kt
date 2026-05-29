package com.example.Run_App.ui.run.quickRun.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PausedRunScreen(
    onResume: () -> Unit,
    onStop: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Paused")

        Spacer(Modifier.height(24.dp))

        Button(onClick = onResume) {
            Text("Resume")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = onStop) {
            Text("Stop")
        }
    }
}

