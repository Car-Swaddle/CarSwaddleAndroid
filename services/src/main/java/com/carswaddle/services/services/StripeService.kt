package com.carswaddle.services.services

import com.carswaddle.carswaddleandroid.services.ContentType
import com.carswaddle.carswaddleandroid.services.serviceModels.Balance
import com.carswaddle.carswaddleandroid.services.serviceModels.Payout
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.Transaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*


private const val ephemeralKeysEndpoint = "/api/stripe/ephemeral-keys"
private const val balance = "/api/stripe/balance"
private const val payouts = "/api/stripe/payouts"
private const val transactions = "/api/stripe/transactions"
private const val transactionDetails = "/api/stripe/transaction-details"

interface StripeService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(ephemeralKeysEndpoint)
    fun getEphemeralKeys(@Query("apiVersion") apiVersion: String): Call<Map<String, Any>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(balance)
    fun getBalance(): Call<Balance>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(payouts)
    fun getPayouts(@Query("startingAfterID") startingAfterId: String?, @Query("status") status: PayoutStatus?, @Query("limit") limit: Int?): Call<PayoutResponse>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(transactions)
    fun getTransactions(@Query("startingAfterID") startingAfterId: String?, @Query("payoutID") payoutId: String?, @Query("limit") limit: Int?): Call<TransactionResponse>
    
}


data class PayoutResponse(
    val has_more: Boolean,
    val data: List<Payout>
)

data class TransactionResponse(
    val has_more: Boolean,
    val data: List<Transaction>
)
