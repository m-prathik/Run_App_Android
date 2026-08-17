package com.example.Run_App.ui.run.quickRun.util

import android.util.Log

fun formatElapsedTime(seconds: Long): String {

    val hours = seconds / 3600
    var funSeconds = seconds % 3600
    val minutes = funSeconds / 60
    funSeconds = funSeconds % 60;
    val remainingSeconds = funSeconds

    Log.d("Timer", "inside countdown function with " +  hours);
    return if (hours > 0) {
        String.format("%02d:%02d:%02d",
            hours,
            minutes,
            remainingSeconds)
    } else {
        String.format(
            "%02d:%02d",
            minutes,
            remainingSeconds
        )
    }
}