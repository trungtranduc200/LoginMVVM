package com.example.loginmvvm.repository

import com.example.loginmvvm.service.DataService

class CategoriesRepository(private val apiService: DataService) {
    suspend fun getListCategory(token:String) =
        apiService.getListCategory(token)
}