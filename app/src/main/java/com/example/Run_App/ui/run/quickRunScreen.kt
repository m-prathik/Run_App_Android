package com.example.Run_App.ui.run

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay

@Composable
fun quickRunScreen (
    fusedLocationClient : FusedLocationProviderClient,
    mapboxToken : String,
    navController: NavController
) {
    var hasPermission by rememberSaveable { mutableStateOf(false) }
    var mapUrl by remember { mutableStateOf<String?>(null) }
    var locationLoaded by remember { mutableStateOf(false) }
    var locationError by remember {mutableStateOf<String?>(null)}
    var askedForGps by remember{mutableStateOf(false)}
    var permissionRequested by rememberSaveable {mutableStateOf(false)}
    var gpsRequested by rememberSaveable {mutableStateOf(false)}
    var context = LocalContext.current
    val activity = context as? Activity
    val gpsResolutionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {result ->
        if(result.resultCode == Activity.RESULT_OK) {
            Log.d("QuickRunScreen", "user enabled gps")
            fetchLocation(context, fusedLocationClient, mapboxToken) { url, error ->
                mapUrl = url
                locationError = error
            }
        } else {
                mapUrl = defaultMapUrl(mapboxToken)
                locationError = "Please enable location services"
            }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        Log.d("QuickRunScreen", "Permission granted :$granted")
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("QuickRunScreen", "permission is true in launched effect ");
            hasPermission = true
        } else if(!permissionRequested) {
            permissionRequested = true
            permissionLauncher.launch(permission)
        }
    }
    //Fetch location after permission is granted
    LaunchedEffect(hasPermission) {
        Log.d("QuickRunScreen", "hasPermission:$hasPermission")
        if (hasPermission && activity != null && !gpsRequested) {
            gpsRequested = true
            checkLocationEnabled(activity, gpsResolutionLauncher) {gpsEnabled ->
                Log.d("QuickRunScreen", "location is turned on")
                if(gpsEnabled) {
                    fetchLocation(context, fusedLocationClient, mapboxToken) {url, error ->
                        mapUrl = url
                        locationError = error
                    }
                } else {
                        Log.d("QuickRunScreen", "Location is null");
                        mapUrl = defaultMapUrl(mapboxToken);
                        locationError = "Gps disabled, showing default map"
                }

            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (mapUrl != null) {
            GlideImage(
                imageModel = { mapUrl },
                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
    fun checkLocationEnabled(
        activity : Activity,
        launcher : ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        onResult: (Boolean)-> Unit
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).build()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(activity);
        val task = client.checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { exception ->
                if(exception is ResolvableApiException) {
                    try {
                        val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                        launcher.launch(intentSenderRequest)
                    } catch (sendEx : IntentSender.SendIntentException) {
                        onResult(false)
                    }
                } else {
                    onResult(false)
            }
        }
    }

fun fetchLocation(
    context: Context,
    fusedLocationClient : FusedLocationProviderClient,
    mapboxToken: String,
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
    fun defaultMapUrl(mapboxToken : String) : String {
       val lat = 22.927
       val lon = 80.912
       val zoom = 3.72
       return "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/$lon,$lat,$zoom,0/800x800@2x?access_token=$mapboxToken"
    }
