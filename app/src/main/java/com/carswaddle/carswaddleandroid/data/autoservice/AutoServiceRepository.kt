package com.carswaddle.carswaddleandroid.data.autoservice

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.location.Location
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.services.AutoServiceService
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


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

    fun getAutoService(autoServiceId: String, context: Context, completion: (error: Error?, autoServiceId: String?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(null, null)
            return
        }

        val call = autoServiceService.autoServices(autoServiceId)
        call.enqueue(object : Callback<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService> {
            override fun onFailure(call: Call<AutoService>, t: Throwable) {
                completion(null, null)
            }

            override fun onResponse(call: Call<AutoService>, response: Response<AutoService>) {
                val autoService = response.body()
                if (autoService == null) {
                    print("no auto service")
                    completion(null, null)
                } else {
                    GlobalScope.async {
                        try {
                            insertNestedAutoService(autoService)
                            completion(null, autoService.id)
                        } catch (e: Exception) {
                            print(e)
                            completion(null, null)
                        }
                    }
                }
            }
        })
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
                        try {
                            var ids = arrayListOf<String>()
                            for (autoService in result) {
                                insertNestedAutoService(autoService)
                                ids.add(autoService.id)
                            }
                            completion(null, ids.toList())
                        } catch(e: Exception) {
                            print(e)
                        }
                    }
                }
            }
        })
    }

    suspend private fun insertNestedAutoService(autoService: com.carswaddle.carswaddleandroid.services.serviceModels.AutoService): com.carswaddle.carswaddleandroid.data.autoservice.AutoService {
        val storedAutoService = com.carswaddle.carswaddleandroid.data.autoservice.AutoService(autoService)
        val location = Location(autoService.location)
        autoServiceDao.insertLocation(location)
        val vehicle = Vehicle(autoService.vehicle)
        autoServiceDao.insertVehicle(vehicle)
        val mechanic = Mechanic(autoService.mechanic)
        autoServiceDao.insertMechanic(mechanic)
        autoService.mechanic.user?.let {
            autoServiceDao.insertUser(User(it))
        }
        // TODO: ServiceEntities
        insert(storedAutoService)
        return storedAutoService
    }

}





