package com.carswaddle.store.transaction

import com.carswaddle.store.transaction.TransactionDao



class TransactionRepository(val transactionDao: TransactionDao) {

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