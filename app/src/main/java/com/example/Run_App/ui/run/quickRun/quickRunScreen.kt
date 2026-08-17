package com.example.Run_App.ui.run.quickRun

import android.Manifest
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.Run_App.ui.run.quickRun.screens.ActiveRunScreen
import com.example.Run_App.ui.run.quickRun.screens.CountdownScreen
import com.example.Run_App.ui.run.quickRun.screens.PausedRunScreen
import com.example.Run_App.ui.run.quickRun.screens.PostRunScreen
import com.example.Run_App.ui.run.quickRun.screens.PreRunScreen
import com.example.Run_App.ui.run.quickRun.state.RunState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun quickRunScreen (
    navController: NavController,
    mapboxToken : String,
    viewModel: QuickRunViewModel = hiltViewModel()
) {
    val TAG = "QuickRunScreen"
    var context = LocalContext.current
    val activity = context as? Activity
    val uiState by viewModel.uiState.collectAsState()

    var hasPermission by rememberSaveable { mutableStateOf(false) }
    var gpsRequested by rememberSaveable { mutableStateOf(false) }

    lateinit var gpsResolutionLauncher : ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    gpsResolutionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {result ->
        if(result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "user enabled gps")
            viewModel.onGpsResolutionResult(true, activity)
//            if(activity != null) viewModel.checkGpsEnabled(activity, gpsResolutionLauncher, mapboxToken)
        } else {
            if(activity != null) {
                viewModel.onGpsResolutionResult(false)
            }
                Log.d(TAG, "User didnot enable GPS")
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        viewModel.onPermissionResult(granted)
        Log.d(TAG, "Permission granted :$granted")
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission is true in launched effect ");
            hasPermission = true
        } else {
            permissionLauncher.launch(permission)
        }
    }
    //Fetch location after permission is granted
    LaunchedEffect(hasPermission) {
        Log.d(TAG, "hasPermission:$hasPermission")
        if (hasPermission && activity != null && !gpsRequested) {
            gpsRequested = true
            viewModel.checkGpsEnabled(activity, gpsResolutionLauncher, mapboxToken)
        }
    }
    when (uiState.runState) {

        RunState.PRE_RUN ->
            PreRunScreen(
                mapUrl = uiState.mapUrl,
                isLoading = uiState.isLoading,
                onRunClick = viewModel::startCountDown
            )

        RunState.COUNTDOWN ->
            CountdownScreen(uiState.countDown)

        RunState.RUNNING ->
            ActiveRunScreen(
                uiState = uiState,
                onPause = viewModel::pauseRun,
                onStop = viewModel::stopRun
            )

        RunState.PAUSED ->
            PausedRunScreen(
                onResume = viewModel::resumeRun,
                onStop = viewModel::stopRun
            )

        RunState.POST_RUN ->
            PostRunScreen(uiState,
                onSaveRun = viewModel::saveRun,
                onDiscardRun = viewModel::discardRun)
    }
}
