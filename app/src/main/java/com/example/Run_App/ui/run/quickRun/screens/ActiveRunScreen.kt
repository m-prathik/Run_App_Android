package com.example.Run_App.ui.run.quickRun.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.Run_App.data.model.QuickRunUiState
import com.example.Run_App.ui.run.quickRun.util.formatElapsedTime

@Composable
fun ActiveRunScreen(
    uiState: QuickRunUiState,
    onPause: () -> Unit,
    onStop: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Metric("Distance",
                String.format("%.2f km", uiState.distanceMeters / 1000f))

            Metric("Time",
                formatElapsedTime(uiState.elapsedTime)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Button(
                onClick = onPause,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                modifier = Modifier.size(110.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Pause",
                    modifier = Modifier.size(60.dp)
                )
            }

//            Button(
//                onClick = onStop,
//                shape = RoundedCornerShape(20.dp),
//                modifier = Modifier.size(120.dp),
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
//            )
//            {
//                Text("Stop",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.ExtraBold
//                )
//            }
        }
    }
}

