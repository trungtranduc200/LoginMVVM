package com.example.loginmvvm.repository

import com.example.loginmvvm.model.LoginRequest
import com.example.loginmvvm.model.UserRequest
import com.example.loginmvvm.service.DataService

class LoginRepository(private val apiService: DataService) {
    suspend fun login(loginRequest: LoginRequest) =
        apiService.login(loginRequest)

    suspend fun signup(userRequest: UserRequest) =
        apiService.signup(userRequest)
}