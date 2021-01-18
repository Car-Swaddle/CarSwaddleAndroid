package com.carswaddle.services.services

import com.carswaddle.carswaddleandroid.services.ContentType
import com.carswaddle.carswaddleandroid.services.serviceModels.Balance
import com.carswaddle.carswaddleandroid.services.serviceModels.Payout
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*


private const val ephemeralKeysEndpoint = "/api/stripe/ephemeral-keys"
private const val balance = "/api/stripe/balance"
private const val payouts = "/api/stripe/payouts"

interface StripeService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(ephemeralKeysEndpoint)
    fun getEphemeralKeys(@Query("apiVersion") apiVersion: String): Call<Map<String, Any>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(balance)
    fun getBalance(): Call<Balance>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(payouts)
    fun getPayoutsMap(@Query("startingAfterID") startingAfterId: String?, @Query("status") status: String?, @Query("limit") limit: Int?): Call<Map<String,Any>>


    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(payouts)
    fun getPayouts(@Query("startingAfterId") startingAfterId: String?, @Query("status") status: PayoutStatus?, @Query("limit") limit: Int?): Call<PayoutResponse>
    
}


data class PayoutResponse(
    val has_more: Boolean,
    val data: List<Payout>
) {
    
}
