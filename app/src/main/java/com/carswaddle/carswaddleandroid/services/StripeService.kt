package com.carswaddle.carswaddleandroid.services

import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


private const val ephemeralKeysEndpoint = "/api/stripe/ephemeral-keys"

interface StripeService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(ephemeralKeysEndpoint)
    fun getEphemeralKeys(@Query("apiVersion") apiVersion: String): Call<Map<String, Any>>
    
}