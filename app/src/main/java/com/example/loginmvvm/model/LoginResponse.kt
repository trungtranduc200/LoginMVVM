package com.example.loginmvvm.model

data class LoginResponse(val user:User,val accessToken:String,val refreshToken:String)
