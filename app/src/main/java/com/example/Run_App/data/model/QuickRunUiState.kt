package com.example.Run_App.data.model

import com.example.Run_App.ui.run.quickRun.state.RunState

data class QuickRunUiState(

    val hasPermission: Boolean = false,
    val gpsEnabled: Boolean = false,
    val gpsRequested: Boolean = false,

    val isLoading: Boolean = false,
    val mapUrl: String? = null,
    val error: String? = null,

    val runState: RunState = RunState.PRE_RUN,

    val countDown: Int = 3,

    val elapsedTime: Long = 0L,
    val currentPace: Float = 0f,
    val averagePace: Float = 0f,
    val distanceMeters: Float = 0f,

    val runId: String? = null
)
