package com.example.loginmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginmvvm.repository.LoginRepository
import com.example.loginmvvm.model.LoginRequest
import com.example.loginmvvm.model.LoginResponse
import com.example.loginmvvm.model.User
import com.example.loginmvvm.model.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {
    val loginResult = MutableLiveData<LoginResponse>()
    val loginError = MutableLiveData<String>()
    val sigUpError = MutableLiveData<String>()

    private fun login(loginRequest: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.login(loginRequest)
                if (response.code() == 200){
                    loginResult.postValue(response.body())
                }else{
                    loginError.postValue("Đăng nhập thất bại")
                }
                Log.d("trungtd11 response",response.message())

            } catch (e: Exception) {
                loginError.postValue(e.message)
            }
        }
    }

    fun signup(userRequest: UserRequest){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.signup(userRequest)
                if (response.code() == 201){
                    login(LoginRequest(userRequest.email,userRequest.password))
                    sigUpError.postValue("Đăng ký account thành công. Đang tiến hành đăng nhập")
                }else if(response.code() == 403){
                    login(LoginRequest(userRequest.email,userRequest.password))
                    sigUpError.postValue("Account đã tồn tại. Đang tiến hành đăng nhập")
                }else{
                    sigUpError.postValue("Có lỗi trong quá trình đăng ký")
                }
            } catch (e: Exception) {
                sigUpError.postValue(e.message)
            }
        }
    }


}