package com.example.Run_App.ui.run.quickRun.util

fun formatPace(secondsPerKm : Float) : String {
    if (secondsPerKm <= 0)  {
       return "--:--  min/km"
    }
    val totalSeconds = secondsPerKm.toInt();
    val minutes = totalSeconds / 60;
    val seconds = totalSeconds % 60;

    return "%02d:%02d  min/km".format(minutes, seconds)

}