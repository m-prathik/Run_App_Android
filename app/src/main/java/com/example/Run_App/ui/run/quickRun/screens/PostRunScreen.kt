package com.example.Run_App.ui.run.quickRun.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Run_App.data.model.QuickRunUiState
import com.example.Run_App.ui.run.quickRun.util.formatElapsedTime

@Composable
fun PostRunScreen(
    uiState: QuickRunUiState,
    onSaveRun : () -> Unit,
    onDiscardRun : () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Run Complete",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(32.dp))

        Metric(
            "Distance",
            String.format("%.2f km", uiState.distanceMeters / 1000f)
        )

        Metric(
            "Duration",
            formatElapsedTime(uiState.elapsedTime)
        )

        Metric(
            "Current Pace",
            uiState.currentPace.toString()
        )

        Metric(
            "Average Pace",
            uiState.averagePace.toString()
        )
        Button(
            onClick = onSaveRun,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Run")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onDiscardRun,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Discard Run")
        }
    }
}

