package com.example.Run_App.data.model

data class LoginRequest(val userName: String, val password: String)
data class LoginResponse(val token : String)