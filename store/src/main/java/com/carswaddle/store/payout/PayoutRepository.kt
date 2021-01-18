package com.carswaddle.store.payout

import android.content.Context
import android.util.Log
import com.carswaddle.carswaddleandroid.retrofit.EmptyResponseBody
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.serviceModels.Payout
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.services.services.PayoutResponse
import com.carswaddle.services.services.StripeService
import com.carswaddle.store.balance.Balance
import com.carswaddle.store.balance.BalanceDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class PayoutRepository(val payoutDao: PayoutDao) {


    fun getPayouts(
        startingAfterId: String? = null,
        status: PayoutStatus? = null,
        limit: Int? = null,
        context: Context,
        completion: (t: Throwable?, payoutIds: List<String>?) -> Unit
    ) {
        val stripeService =
            ServiceGenerator.authenticated(context)?.retrofit?.create(StripeService::class.java)
        if (stripeService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }

        val call = stripeService.getPayouts(startingAfterId, status, limit)
        call.enqueue(object :
            Callback<PayoutResponse> {

            override fun onFailure(call: Call<PayoutResponse>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<PayoutResponse>,
                response: Response<PayoutResponse>
            ) {
                val payoutResponse = response.body()
                if (payoutResponse == null) {
                    completion(EmptyResponseBody(), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        var payoutIds: MutableList<String> = mutableListOf()
                        for (payout in payoutResponse.data) {
                            try {
                                insertPayout(payout)
                            } catch (e: Exception) {
                                print("couln't do nothin: $e")
                            }
                            payoutIds.add(payout.id)
                        }
                        completion(null, payoutIds)
                    }
                }
            }

        })
    }

    suspend private fun insertPayout(payout: Payout) {
        payoutDao.insertPayout(com.carswaddle.store.payout.Payout(payout))
    }

    fun payoutSumInTransit(): Int {
        return payoutDao.getInTransitPayoutAmount()
    }

}