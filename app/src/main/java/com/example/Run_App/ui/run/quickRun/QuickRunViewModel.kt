package com.example.Run_App.ui.run.quickRun

import android.app.Activity
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Run_App.BuildConfig
import com.example.Run_App.data.RetrofitClient
import com.example.Run_App.data.model.NewRunRequest
import com.example.Run_App.data.model.QuickRunUiState
import com.example.Run_App.data.repository.RunRepository
import com.example.Run_App.ui.run.quickRun.state.RunState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickRunViewModel @Inject constructor(
    private val repository : RunRepository
    ) : ViewModel() {

    private var timerJob: Job? = null

    private val _uiState: MutableStateFlow<QuickRunUiState> = MutableStateFlow(QuickRunUiState())
    val uiState : StateFlow<QuickRunUiState> = _uiState

    fun onPermissionResult(granted : Boolean) {
        _uiState.value = _uiState.value.copy(hasPermission = granted)
    }

    fun onGpsResolutionResult(success : Boolean, activity : Activity? = null) {
        Log.d("viewModel", "success is $success and activity is $activity")
        if(success && activity != null) {
            _uiState.value = _uiState.value.copy(gpsEnabled = true)
            fetchMap(activity, BuildConfig.MAPBOX_TOKEN)
        }
        else {
            _uiState.value = _uiState.value.copy(
                gpsEnabled = false,
                mapUrl = repository.defaultMapUrl(),
                error = "Gps disabled, showing default map"
            )
        }
    }

    fun checkGpsEnabled(
        activity : Activity,
        launcher : ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        mapboxToken : String
    ) {
        _uiState.value = _uiState.value.copy(gpsRequested = true)
        repository.checkLocationEnabled(activity, launcher) { gpsEnabled ->
            Log.d("viewModel", "GPS result is $gpsEnabled")
            _uiState.value = _uiState.value.copy(gpsEnabled = gpsEnabled)
            if (gpsEnabled) {
                Log.d("viewModel", "Fetching actual location")
                fetchMap(activity, mapboxToken)
            } else {
                val url = repository.defaultMapUrl(mapboxToken)
                Log.d("viewModel", "default url is $url")
                _uiState.value = _uiState.value.copy(
                    mapUrl = url,
                    error = "Gps disabled, showing default map"
                )
            }
        }
    }

    private fun fetchMap(context : Activity, mapboxToken : String) {
        Log.d("viewModel", "fetch map called")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.fetchLocation(context, mapboxToken) {url, error ->
                _uiState.value = _uiState.value.copy(
                    mapUrl = url,
                    error = error,
                    isLoading = false
                )
            }
        }
    }

    fun startCountDown() {
        Log.d("viewModel", "inside countdown function");
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                runState = RunState.COUNTDOWN,
                countDown = 3
            )
            for (i in 3 downTo 1) {
                _uiState.value = _uiState.value.copy(countDown = i);
                delay(1000)
            }

            _uiState.value = _uiState.value.copy(
                runState = RunState.RUNNING
            )
            startTimer()
            startRun()
        }
    }
    private fun startTimer() {

        timerJob?.cancel()

        timerJob = viewModelScope.launch {

            while (true) {

                delay(1000)

                _uiState.value = _uiState.value.copy(
                    elapsedTime = _uiState.value.elapsedTime + 1
                )
            }
        }
    }

    private fun startRun() {
        viewModelScope.launch {

            // Backend call
            try {
                val response = RetrofitClient.api.createRun(NewRunRequest("temp"))
                _uiState.value = _uiState.value.copy(
                   runId = response.runId
                )
            } catch (e : Exception) {
            }
//            val runId = repository.createRun()

            // Start timer
           // startTimer()

            // Start GPS updates
            //startTracking()
        }
    }
    fun pauseRun() {

        timerJob?.cancel()

        _uiState.value = _uiState.value.copy(
            runState = RunState.PAUSED
        )

        repository.stopLocationTracking()
    }

    fun resumeRun() {

        _uiState.value = _uiState.value.copy(
            runState = RunState.RUNNING
        )

        startTimer()

        repository.startLocationTracking()
    }

    fun stopRun() {

        timerJob?.cancel()

        repository.stopLocationTracking()

        _uiState.value = _uiState.value.copy(
            runState = RunState.POST_RUN
        )
    }
}