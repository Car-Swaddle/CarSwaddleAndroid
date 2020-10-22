package com.carswaddle.carswaddleandroid.data.autoservice

import android.content.Context
import android.util.Log
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocation
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntity
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.services.AutoServiceService
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateAutoService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*


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

    fun updateNotes(autoServiceId: String, notes: String, context: Context, completion: (error: Error?, autoServiceId: String?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(null, null)
            return
        }

        val updateAutoService = UpdateAutoService(null, notes, null, null, null, null, null)

        val call = autoServiceService.updateAutoService(autoServiceId, updateAutoService)
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

    fun getAutoService(autoServiceId: String, context: Context, cacheCompletion: (autoServiceId: String) -> Unit, completion: (error: Error?, autoServiceId: String?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(null, null)
            return
        }

        GlobalScope.async {
            val autoService = getAutoService(autoServiceId)
            if (autoService != null) {
                cacheCompletion(autoServiceId)
            }
        }

        val call = autoServiceService.autoServiceDetails(autoServiceId)
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
                            Log.d("retrofit ", "error persisting auto service" + e)
                        }
                    }
                }
            }
        })
    }

    fun getAutoServicesDate(mechanicId: String, startDate: Calendar, endDate: Calendar, filterStatus: List<AutoServiceStatus>, context: Context, completion: (exception: Exception?, autoServiceIds: List<String>?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(null, null)
            return
        }
        
        val call = autoServiceService.autoServiceDate(mechanicId, startDate, endDate, filterStatus.map { it.name }) // .map { it.name }
        call.enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t as Exception?, null)
            }

            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
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
//                                insertNestedAutoService(autoService)
//                                ids.add(autoService.id)
                            }
                            completion(null, ids.toList())
                        } catch(e: Exception) {
                            print(e)
                            Log.d("retrofit ", "error persisting auto service" + e)
                        }
                    }
                }
            }
        })
    }

    suspend private fun insertNestedAutoService(autoService: com.carswaddle.carswaddleandroid.services.serviceModels.AutoService): com.carswaddle.carswaddleandroid.data.autoservice.AutoService {
        val storedAutoService = AutoService(autoService)
        val location = AutoServiceLocation(autoService.location)
        autoServiceDao.insertLocation(location)
        val vehicle = Vehicle(autoService.vehicle)
        autoServiceDao.insertVehicle(vehicle)
        val mechanic = Mechanic(autoService.mechanic)
        autoServiceDao.insertMechanic(mechanic)
        autoService.mechanic.user?.let {
            autoServiceDao.insertUser(User(it))
        }
        for (serviceEntityServiceModel in autoService.serviceEntities) {
            val serviceEntity = ServiceEntity(serviceEntityServiceModel)
            autoServiceDao.insertServiceEntity(serviceEntity)
            val oilChange = OilChange(serviceEntityServiceModel.oilChange)
            autoServiceDao.insertOilChange(oilChange)
        }

        insert(storedAutoService)
        return storedAutoService
    }

}





