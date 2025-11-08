package com.example.Run_App.data.model

data class QuickRunUiState(
    val hasPermission: Boolean = false,
    val gpsEnabled : Boolean = false,
    val gpsRequested: Boolean = false,
    val isLoading : Boolean = false,
    val mapUrl : String? = null,
    val error : String? = null
)
