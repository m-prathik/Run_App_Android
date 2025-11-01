package com.example.Run_App.data

import com.example.Run_App.data.model.LoginRequest
import com.example.Run_App.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/users/login")
    suspend fun login(@Body request : LoginRequest) : LoginResponse
}