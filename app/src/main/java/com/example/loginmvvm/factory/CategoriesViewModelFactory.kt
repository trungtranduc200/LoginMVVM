package com.example.loginmvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.repository.CategoriesRepository
import com.example.loginmvvm.repository.LoginRepository
import com.example.loginmvvm.viewmodel.CategoriesViewModel
import com.example.loginmvvm.viewmodel.LoginViewModel

class CategoriesViewModelFactory(private val repository: CategoriesRepository): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}