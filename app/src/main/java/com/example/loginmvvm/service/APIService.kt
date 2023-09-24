package com.example.loginmvvm.service

object APIService {
    private const val baseURL = "http://streaming.nexlesoft.com:3001/"

    //khi tuong ta du lieu se duoc tra ve cho dataservice
    val service: DataService
        get() = APIRetrofitClient.getClient(baseURL)!!.create(DataService::class.java)
}