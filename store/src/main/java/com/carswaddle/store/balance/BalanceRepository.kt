package com.carswaddle.store.balance

import android.content.Context
import android.util.Log
import com.carswaddle.carswaddleandroid.retrofit.EmptyResponseBody
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.Balance as BalanceServiceModel
import com.carswaddle.services.services.StripeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BalanceRepository(val balanceDao: BalanceDao) {

    suspend fun getBalance(mechanicId: String): Balance? {
        return balanceDao.getBalance(mechanicId)
    }
    
    suspend fun insertBalance(balance: Balance) {
        return balanceDao.insertBalance(balance)
    }
    
    fun getBalance(mechanicId: String, context: Context, completion: (error: Throwable?) -> Unit) {
        val stripeService = ServiceGenerator.authenticated(context)?.retrofit?.create(StripeService::class.java)
        if (stripeService == null) {
            completion(ServiceNotAvailable())
            return
        }
        
        val call = stripeService.getBalance()
        call.enqueue(object : Callback<BalanceServiceModel> {
            override fun onFailure(call: Call<BalanceServiceModel>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t)
            }

            override fun onResponse(
                call: Call<BalanceServiceModel>,
                response: Response<BalanceServiceModel>
            ) {
                Log.d("retrofit ", "Balance call success")
                val balance = response.body()
                if (balance == null) {
                    completion(EmptyResponseBody())
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val newBalance = Balance(balance, mechanicId)
                            insertBalance(newBalance)
                            completion(null)
                        } catch (e: Exception) {
                            print(e)
                            Log.d("retrofit ", "error persisting balance" + e)
                        }
                    }
                }
            }
        })


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
        
    }
    
}