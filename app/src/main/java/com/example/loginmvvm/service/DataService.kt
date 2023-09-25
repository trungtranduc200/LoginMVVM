package com.example.loginmvvm.service

import com.example.loginmvvm.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DataService {
    @POST("auth/signin")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/signup")
    suspend fun signup(@Body request: UserRequest): Response<User>

    @GET("categories")
    suspend fun getListCategory(@Header("Authorization") token: String): Response<List<ItemModel>>
}