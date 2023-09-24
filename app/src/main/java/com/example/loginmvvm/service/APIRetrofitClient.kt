package com.example.loginmvvm.service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIRetrofitClient {
    private var retrofit: Retrofit? = null
    fun getClient(baseURL: String?): Retrofit? {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(
                30000,
                TimeUnit.MILLISECONDS
            )
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(
                30000,
                TimeUnit.MILLISECONDS
            )
            .retryOnConnectionFailure(true)
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()
        val gson =
            GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit
    }
}