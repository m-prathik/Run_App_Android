package com.example.Run_App.data

import com.example.Run_App.data.model.LoginRequest
import com.example.Run_App.data.model.LoginResponse
import com.example.Run_App.data.model.NewRunRequest
import com.example.Run_App.data.model.NewRunResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/users/login")
    suspend fun login(@Body request : LoginRequest) : LoginResponse

    @POST("/run/createRun")
    suspend fun createRun(@Body request: NewRunRequest) : NewRunResponse
}