package com.example.Run_App.ui.run.quickRun.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PreRunScreen(
    mapUrl: String?,
    isLoading: Boolean,
    onRunClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (isLoading) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

        } else {

            GlideImage(
                imageModel = { mapUrl },
                modifier = Modifier.fillMaxSize()
            )
        }

        Button(
            onClick = onRunClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .size(100.dp)
        ) {

            Text(
                text = "Run",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

