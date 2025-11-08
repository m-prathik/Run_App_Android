package com.example.Run_App.data.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.Run_App.BuildConfig
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunRepository @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
){
    fun checkLocationEnabled(
        context: Context,
        launcher : ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        onResult : (Boolean) -> Unit
    ) {
        Log.d("Repo", "checkLocationEnabled() called")
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        Log.d("Repo", "gpsEnabled (initial) = $gpsEnabled")
        if(gpsEnabled) {
            Log.d("Repo", "GPS already enabled → returning true")
            onResult(true)
            return
        }
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).build()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(context);
        val task = client.checkLocationSettings(builder.build())

        task
            .addOnSuccessListener {
                Log.d("Repo", "addOnSuccessListener triggered")
               val nowEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                Log.d("Repo", "nowEnabled = $nowEnabled")
                onResult(nowEnabled) }
            .addOnFailureListener { exception ->
                Log.d("Repo", "addOnFailureListener triggered: ${exception.message}")
                if(exception is ResolvableApiException) {
                    try {
                        Log.d("Repo", "Launching resolution dialog")
                        val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                        launcher.launch(intentSenderRequest)
                    } catch (sendEx : IntentSender.SendIntentException) {
                        Log.d("Repo", "SendIntentException: ${sendEx.message}")
                        onResult(false)
                    }
                } else {
                    Log.d("Repo", "Non-resolvable failure → false")
                    onResult(false)
                }
            }
    }

    fun fetchLocation(
        context : Context,
        mapboxToken : String,
        callback : (String?, String?) -> Unit
    ) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                callback(null, "Location Permission not granted")
                return
            }

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        val zoom = 16
                        val url =
                            "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/" +
                                    "pin-s($lon,$lat)/$lon,$lat,$zoom,0/800x800@2x?access_token=$mapboxToken"
                        callback(url, null)
                    } else {
                        callback(null, "Could not get location. Try again.")
                    }
                }
                .addOnFailureListener {
                    callback(null, "Failed to get location: ${it.localizedMessage}")
                }
        } catch (se: SecurityException) {
            callback(null, "Location Request access denied by system ${se.localizedMessage}")
        }
    }

    fun defaultMapUrl(mapboxToken : String = BuildConfig.MAPBOX_TOKEN) : String {
        val lat = 22.927
        val lon = 80.912
        val zoom = 3.72
        return "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/$lon,$lat,$zoom,0/800x800@2x?access_token=$mapboxToken"
    }
}