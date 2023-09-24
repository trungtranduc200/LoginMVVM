package com.example.loginmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginmvvm.model.ItemModel
import com.example.loginmvvm.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: CategoriesRepository) : ViewModel(){
    val categoriesResult = MutableLiveData<List<ItemModel>>()
    fun getListCategory(token:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = repository.getListCategory(token)
                categoriesResult.postValue(listOf())
            } catch (e: Exception) {
                categoriesResult.postValue(listOf())
            }
        }
    }
}