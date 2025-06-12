package com.example.Run_App.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Run_App.data.RetrofitClient
import com.example.Run_App.data.model.LoginRequest
import com.example.Run_App.data.model.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val loginResult = MutableLiveData<Result<LoginResponse>>()

    fun login(userName: String, password : String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.login(LoginRequest(userName, password))
                loginResult.value = Result.success(response)
            } catch (e : Exception) {
                loginResult.value = Result.failure(e)
            }
        }
    }
}