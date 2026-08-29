package com.example.Run_App.data.domain

import android.location.Location
import android.util.Log
import com.example.Run_App.data.model.LocationPoint
import com.example.Run_App.data.model.RunMetrics

class RunMetricsCalculator {
    fun calculate(
        points : List<LocationPoint>
    ) : RunMetrics {
        if (points.size < 2) {
            return RunMetrics()
        }
        var totalDistance = 0f
        for (i in 1 until points.size) {
            val previous = points[i - 1]
            val current = points[i]

            totalDistance += distanceBetween(previous, current)

        }

        val elapsedTime = (points.last().timeStamp - points.first().timeStamp) / 1000

        val averagePace = if (totalDistance > 0) {
            Log.d("Metrics", " elapsed time is ${elapsedTime.toFloat()}, total distance is $totalDistance")
            elapsedTime.toFloat() / (totalDistance / 1000f)
        } else {
            0f
        }
        return RunMetrics(
            distanceMeters = totalDistance,
            elapsedTime = elapsedTime,
            averagePace = averagePace
        )
    }

    private fun distanceBetween(
        previous : LocationPoint,
        current : LocationPoint
    ) : Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            previous.latitude,
            previous.longitude,
            current.latitude,
            current.longitude,
            results
        )
        return results[0]
    }
}