package com.example.reservasport.data

import retrofit2.http.GET

interface CanchaApiService {
    @GET("posts")
    suspend fun obtenerCanchas(): List<CanchaRemota>
}