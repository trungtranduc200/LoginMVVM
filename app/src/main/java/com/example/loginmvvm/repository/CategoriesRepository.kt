package com.example.loginmvvm.repository

import com.example.loginmvvm.service.DataService
import com.example.loginmvvm.util.UtilHelper

class CategoriesRepository(private val apiService: DataService) {
    suspend fun getListCategory(token:String) =
        apiService.getListCategory(UtilHelper.checkIsBearerToken(token))
}