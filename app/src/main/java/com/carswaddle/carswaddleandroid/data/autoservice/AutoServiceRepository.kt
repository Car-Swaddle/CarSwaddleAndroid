package com.carswaddle.carswaddleandroid.data.autoservice

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.services.AutoServiceService
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AutoServiceRepository(private val autoServiceDao: AutoServiceDao) {

    suspend fun insert(autoService: com.carswaddle.carswaddleandroid.data.autoservice.AutoService) {
        autoServiceDao.insertAutoService(autoService)
    }

    suspend fun getAutoServices(autoServiceIds: Array<String>): LiveData<Array<com.carswaddle.carswaddleandroid.data.autoservice.AutoService>> {
        return autoServiceDao.getAutoServicesWithIds(autoServiceIds)
    }

    /**
    case scheduled
    case canceled
    case inProgress
    case completed
     */
    fun getAutoServices(limit: Int, offset: Int, context: Context, status: Array<String>, completion: (error: Error?, autoServiceIds: Array<String>?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(null, null)
            return
        }

        val call = autoServiceService.autoServices(limit, offset, arrayOf<String>())
        call.enqueue(object : Callback<Array<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>> {
            override fun onFailure(call: Call<Array<AutoService>>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t as Error?, null)
            }

            override fun onResponse(call: Call<Array<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>>, response: Response<Array<AutoService>>) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result == null) {
                    // TODO: make an error here
                    completion(null, null) // somethind
                } else {
                    GlobalScope.async {
                        var ids = arrayListOf<String>()
                        for (autoService in result) {
                            val autoService = com.carswaddle.carswaddleandroid.data.autoservice.AutoService(autoService)
                            ids.add(autoService.id)
                            insert(autoService)
                        }
                        completion(null, ids.toTypedArray())
                    }
                }
            }
        })
    }

}





