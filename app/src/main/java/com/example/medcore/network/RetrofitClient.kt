package com.example.medcore.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    // For emulator use 10.0.2.2, for physical device use your laptop's local IP
    private const val BASE_URL = "http://192.168.40.13:8000/"

    val paymentApi: PaymentApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentApi::class.java)
    }
}