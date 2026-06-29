package com.example.reservasport.data

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val TAG = "RetrofitClient"
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: CanchaApiService by lazy {
        Log.d(TAG, "Inicializando cliente Retrofit con BASE_URL: $BASE_URL")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CanchaApiService::class.java)
    }
}