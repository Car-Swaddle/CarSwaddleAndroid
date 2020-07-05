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

    suspend fun getAutoServices(autoServiceIds: List<String>): List<com.carswaddle.carswaddleandroid.data.autoservice.AutoService> {
        return autoServiceDao.getAutoServicesWithIds(autoServiceIds)
    }

    suspend fun getAutoService(autoServiceId: String): com.carswaddle.carswaddleandroid.data.autoservice.AutoService? {
        return autoServiceDao.getAutoService(autoServiceId)
    }

    /**
    case scheduled
    case canceled
    case inProgress
    case completed
     */
    fun getAutoServices(limit: Int, offset: Int, context: Context, sortStatus: List<String>, filterStatus: List<String>, completion: (error: Error?, autoServiceIds: List<String>?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(null, null)
            return
        }

        val call = autoServiceService.autoServices(limit, offset, sortStatus, filterStatus)
        call.enqueue(object : Callback<List<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>> {
            override fun onFailure(call: Call<List<AutoService>>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t as Error?, null)
            }

            override fun onResponse(call: Call<List<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>>, response: Response<List<AutoService>>) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result == null) {
                    // TODO: make an error here
                    completion(null, null) // something
                } else {
                    GlobalScope.async {
                        var ids = arrayListOf<String>()
                        for (autoService in result) {
                            val autoService = com.carswaddle.carswaddleandroid.data.autoservice.AutoService(autoService)
                            ids.add(autoService.id)
                            insert(autoService)
                        }
                        completion(null, ids.toList())
                    }
                }
            }
        })
    }

}





