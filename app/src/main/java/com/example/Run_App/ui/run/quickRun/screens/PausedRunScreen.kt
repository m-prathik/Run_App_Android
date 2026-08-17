package com.example.Run_App.ui.run.quickRun.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PausedRunScreen(
    onResume: () -> Unit,
    onStop: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        Text("Paused")

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Surface(
                modifier = Modifier.size(110.dp),
                shape = CircleShape,
                color = Color.Black
            ) {
                IconButton(
                    onClick = onResume,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume",
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            Surface(
                modifier = Modifier.size(110.dp),
                shape = CircleShape,
                color = Color.Red
            ) {
                IconButton(
                    onClick = onStop,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Stop Run",
                        modifier = Modifier.size(50.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

