package com.carswaddle.store.transaction

import android.content.Context
import com.carswaddle.carswaddleandroid.retrofit.EmptyResponseBody
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.serviceModels.Transaction
import com.carswaddle.services.services.StripeService
import com.carswaddle.services.services.TransactionResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class TransactionRepository(val transactionDao: TransactionDao) {


    fun getTransactions(startingAfterId: String? = null, payoutId: String? = null, limit: Int = 10, context: Context, completion: (t: Throwable?, transactionIds: List<String>?) -> Unit) {
        val stripeService = ServiceGenerator.authenticated(context)?.retrofit?.create(StripeService::class.java)
        if (stripeService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }

        val call = stripeService.getTransactions(startingAfterId, payoutId, limit)

        call.enqueue( object :
            Callback<TransactionResponse> {

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<TransactionResponse>,
                response: Response<TransactionResponse>
            ) {
                val transactionsResponse = response.body()
                if (transactionsResponse == null) {
                    completion(EmptyResponseBody(), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        var transactionIds: MutableList<String> = mutableListOf()
                        for (transaction in transactionsResponse.data) {
                            try {
                                insertTransaction(transaction)
                            } catch (e: Exception) {
                                print("couln't do nothin: $e")
                            }
                            transactionIds.add(transaction.id)
                        }
                        completion(null, transactionIds)
                    }
                }
            }

        })

    }
    
    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(com.carswaddle.store.transaction.Transaction(transaction))
    }
    
    suspend fun getTransactions(ids: List<String>): List<com.carswaddle.store.transaction.Transaction> {
        return transactionDao.getTransaction(ids)
    }

//    suspend fun getBalance(mechanicId: String): Balance? {
//        return balanceDao.getBalance(mechanicId)
//    }
//
//    suspend fun insertBalance(balance: Balance) {
//        return balanceDao.insertBalance(balance)
//    }
//
//    fun getBalance(mechanicId: String, context: Context, completion: (error: Throwable?) -> Unit) {
//        val stripeService = ServiceGenerator.authenticated(context)?.retrofit?.create(StripeService::class.java)
//        if (stripeService == null) {
//            completion(ServiceNotAvailable())
//            return
//        }
//
//        val call = stripeService.getBalance()
//        call.enqueue(object :
//            Callback<com.carswaddle.carswaddleandroid.services.serviceModels.Balance> {
//            override fun onFailure(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.Balance>, t: Throwable) {
//                Log.d("retrofit ", "call failed")
//                completion(t)
//            }
//
//            override fun onResponse(
//                call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.Balance>,
//                response: Response<com.carswaddle.carswaddleandroid.services.serviceModels.Balance>
//            ) {
//                Log.d("retrofit ", "Balance call success")
//                val balance = response.body()
//                if (balance == null) {
//                    completion(EmptyResponseBody())
//                } else {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        try {
//                            val newBalance = Balance(balance, mechanicId)
//                            insertBalance(newBalance)
//                            completion(null)
//                        } catch (e: Exception) {
//                            print(e)
//                            Log.d("retrofit ", "error persisting balance" + e)
//                        }
//                    }
//                }
//            }
//        })


//        val call = stripeService.getBalance()
//        call.enqueue(object : Callback<Map<String,Any>> {
//            override fun onFailure(call: Call<Map<String,Any>>, t: Throwable) {
//                Log.d("retrofit ", "call failed")
//                completion(t)
//            }
//
//            override fun onResponse(
//                call: Call<Map<String,Any>>,
//                response: Response<Map<String,Any>>
//            ) {
//                Log.d("retrofit ", "Balance call success")
//                val balance = response.body()
//                if (balance == null) {
//                    completion(EmptyResponseBody())
//                } else {
////                    CoroutineScope(Dispatchers.IO).launch {
////                        try {
////                            val newBalance = Balance(balance, mechanicId)
////                            insertBalance(newBalance)
////                            completion(null)
////                        } catch (e: Exception) {
////                            print(e)
////                            Log.d("retrofit ", "error persisting balance" + e)
////                        }
////                    }
//                }
//            }
//        })
//
//    }

}