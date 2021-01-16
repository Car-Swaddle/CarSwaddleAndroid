package com.carswaddle.services.services

import com.carswaddle.carswaddleandroid.services.ContentType
import com.carswaddle.carswaddleandroid.services.serviceModels.Balance
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


private const val ephemeralKeysEndpoint = "/api/stripe/ephemeral-keys"
private const val balance = "/api/stripe/balance"

interface StripeService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(ephemeralKeysEndpoint)
    fun getEphemeralKeys(@Query("apiVersion") apiVersion: String): Call<Map<String, Any>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(balance)
    fun getBalance(): Call<Balance>
    
//    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
//    @GET(balance)
//    fun getBalance(): Call<Map<String,Any>>
    
}