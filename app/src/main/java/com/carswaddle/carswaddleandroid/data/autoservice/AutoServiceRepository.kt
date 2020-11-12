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
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.AutoServiceService
import com.carswaddle.carswaddleandroid.services.LocationJSON
import com.carswaddle.carswaddleandroid.services.PriceRequest
import com.carswaddle.carswaddleandroid.services.PriceService
import com.carswaddle.carswaddleandroid.services.serviceModels.*
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
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
    
    fun getPrice(location: LocationJSON, mechanicId: String, oiltype: OilType, coupon: String?, context: Context, completion: (error: Throwable?, price: Price?) -> Unit) {
        val priceService = ServiceGenerator.authenticated(context)?.retrofit?.create(PriceService::class.java)
        if (priceService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }
        
        val priceRequest = PriceRequest(location, mechanicId, oiltype.toString(), coupon)
        val call = priceService.getPrice(priceRequest)
        call.enqueue(object : Callback<PriceResponse> {

            override fun onFailure(call: Call<PriceResponse>, t: Throwable) {
                completion(t, null)
            }
 
            override fun onResponse(call: Call<PriceResponse>, response: Response<PriceResponse>) {
                completion(null, response.body()?.prices)
            }
            
        })
    }

    fun updateNotes(autoServiceId: String, notes: String, context: Context, completion: (error: Throwable?, autoServiceId: String?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }

        val updateAutoService = UpdateAutoService(null, notes, null, null, null, null, null)

        val call = autoServiceService.updateAutoService(autoServiceId, updateAutoService)
        call.enqueue(object : Callback<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService> {
            override fun onFailure(call: Call<AutoService>, t: Throwable) {
                completion(t, null)
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
                            completion(e, null)
                        }
                    }
                }
            }
        })
    }

    fun getAutoService(autoServiceId: String, context: Context, cacheCompletion: (autoServiceId: String) -> Unit, completion: (error: Throwable?, autoServiceId: String?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable(), null)
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
                completion(t, null)
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
                            completion(e, null)
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
    fun getAutoServices(limit: Int, offset: Int, context: Context, sortStatus: List<String>, filterStatus: List<String>, completion: (error: Throwable?, autoServiceIds: List<String>?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable(), null)
            return
        }

        val call = autoServiceService.autoServices(limit, offset, sortStatus, filterStatus)
        call.enqueue(object : Callback<List<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>> {
            override fun onFailure(call: Call<List<AutoService>>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(call: Call<List<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>>, response: Response<List<AutoService>>) {
                Log.d("retrofit ", "call succeeded")
                val result = response.body()
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

    fun getAutoServicesDate(mechanicId: String, startDate: Calendar, endDate: Calendar, filterStatus: List<AutoServiceStatus>, context: Context, completion: (exception: Throwable?, autoServiceIds: List<String>?) -> Unit): Call<List<Map<String, Any>>>? {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable(), null)
            return null
        }
        
        val call = autoServiceService.autoServiceDate(mechanicId, startDate, endDate, filterStatus.map { it.name })
        call.enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                Log.d("retrofit ", "call succeeded")
                val result = response.body()
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
        
        return call
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





