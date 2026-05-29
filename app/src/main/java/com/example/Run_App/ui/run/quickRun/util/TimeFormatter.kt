package com.example.Run_App.ui.run.quickRun.util

fun formatElapsedTime(seconds: Long): String {

    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return String.format(
        "%02d:%02d",
        minutes,
        remainingSeconds
    )
}