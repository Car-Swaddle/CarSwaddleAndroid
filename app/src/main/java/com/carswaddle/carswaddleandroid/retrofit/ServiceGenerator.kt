package com.carswaddle.carswaddleandroid.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val serviceGenerator = ServiceGenerator("https://api.carswaddle.com")

class ServiceGenerator(baseURL: String) {

    val baseURL: String
    val retrofit: Retrofit

    init {
        this.baseURL = baseURL
        this.retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}