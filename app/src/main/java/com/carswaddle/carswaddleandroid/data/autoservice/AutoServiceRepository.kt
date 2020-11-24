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
import com.carswaddle.carswaddleandroid.services.*
import com.carswaddle.carswaddleandroid.services.serviceModels.*
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService as DataAutoService
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AutoServiceRepository(private val autoServiceDao: AutoServiceDao) {

    suspend fun insert(autoService: com.carswaddle.carswaddleandroid.data.autoservice.AutoService) {
        autoServiceDao.insertAutoService(autoService)
    }

    suspend fun getAutoServices(autoServiceIds: List<String>): List<DataAutoService> {
        return autoServiceDao.getAutoServicesWithIds(autoServiceIds)
    }

    suspend fun getAutoService(autoServiceId: String): DataAutoService? {
        return autoServiceDao.getAutoService(autoServiceId)
    }
    
    fun createAndPayForAutoService(createAutoService: CreateAutoService, context: Context, completion: (error: Throwable?) -> Unit) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(AutoServiceService::class.java)
        if (autoServiceService == null) {
            completion(ServiceNotAvailable())
            return
        }
        
        val call = autoServiceService.createAutoService(createAutoService)
        call.enqueue(object :
            Callback<AutoService> {

            override fun onFailure(call: Call<AutoService>, t: Throwable) {
                completion(t)
            }

            override fun onResponse(call: Call<AutoService>, response: Response<AutoService>) {
                Log.w("new auto service", "new autoservice")
                val body = response.body()
                if (body != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        completion(null)
                    }
                } else {
                    completion(EmptyBodyError())
                }
            }
            
        })
    }
    
    fun getPrice(
        location: LocationJSON,
        mechanicId: String,
        oiltype: OilType,
        coupon: String?,
        context: Context,
        completion: (error: Throwable?, price: Price?) -> Unit
    ) {
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
                val error = response.errorBody()
                if (error != null) {
                    val errorString = error.string()
                    try {
                        val errorJSON = JSONObject(errorString)
                        val errorCode = errorJSON["code"] as String?
                        if (errorCode != null) {
                            val codeType = CouponErrorType.valueOf(errorCode)
                            val couponError = CouponError(codeType)
                            completion(couponError, null)
                        } else {
                            completion(CouponError(CouponErrorType.OTHER), null)
                        }
                    } catch (e: JSONException) {
                        completion(e, null)
                    }
                } else {
                    completion(null, response.body()?.prices)
                }
            }

        })
    }

    fun updateNotes(
        autoServiceId: String,
        notes: String,
        context: Context,
        completion: (error: Throwable?, autoServiceId: String?) -> Unit
    ) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(
            AutoServiceService::class.java
        )
        if (autoServiceService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }

        val updateAutoService = UpdateAutoService(null, notes, null, null, null, null, null)

        val call = autoServiceService.updateAutoService(autoServiceId, updateAutoService)
        call.enqueue(object :
            Callback<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService> {
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

    fun getAutoService(
        autoServiceId: String,
        context: Context,
        cacheCompletion: (autoServiceId: String) -> Unit,
        completion: (error: Throwable?, autoServiceId: String?) -> Unit
    ) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(
            AutoServiceService::class.java
        )
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
        call.enqueue(object :
            Callback<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService> {
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
    fun getAutoServices(
        limit: Int,
        offset: Int,
        context: Context,
        sortStatus: List<String>,
        filterStatus: List<String>,
        completion: (error: Throwable?, autoServiceIds: List<String>?) -> Unit
    ) {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(
            AutoServiceService::class.java
        )
        if (autoServiceService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable(), null)
            return
        }

        val call = autoServiceService.autoServices(limit, offset, sortStatus, filterStatus)
        call.enqueue(object :
            Callback<List<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>> {
            override fun onFailure(call: Call<List<AutoService>>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(
                call: Call<List<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>>,
                response: Response<List<AutoService>>
            ) {
                Log.d("retrofit ", "call succeeded")
                val result = response.body()
                if (result == null) {
                    // TODO: make an error here
                    completion(null, null) // something
                } else {
                    GlobalScope.async {
//                        try {
                            var ids = arrayListOf<String>()
                            for (autoService in result) {
                                try {
                                    insertNestedAutoService(autoService)
                                    ids.add(autoService.id)
                                } catch(e: Exception) { }
                            }
                            completion(null, ids.toList())
//                        } catch (e: Exception) {
//                            print(e)
//                            Log.d("retrofit ", "error persisting auto service" + e)
//                        }
                    }
                }
            }
        })
    }

    fun getAutoServicesDate(
        mechanicId: String,
        startDate: Calendar,
        endDate: Calendar,
        filterStatus: List<AutoServiceStatus>,
        context: Context,
        completion: (exception: Throwable?, autoServiceIds: List<String>?) -> Unit
    ): Call<List<Map<String, Any>>>? {
        val autoServiceService = ServiceGenerator.authenticated(context)?.retrofit?.create(
            AutoServiceService::class.java
        )
        if (autoServiceService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable(), null)
            return null
        }
        
        val call = autoServiceService.autoServiceDate(
            mechanicId,
            startDate,
            endDate,
            filterStatus.map { it.name })
        call.enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(
                call: Call<List<Map<String, Any>>>,
                response: Response<List<Map<String, Any>>>
            ) {
                Log.d("retrofit ", "call succeeded")
                val result = response.body()
                if (result == null) {
                    // TODO: make an error here
                    completion(null, null) // something
                } else {
                    GlobalScope.async {
                        try {
                            var ids = arrayListOf<String>()
//                            for (autoService in result) {
//                                insertNestedAutoService(autoService)
//                                ids.add(autoService.id)
//                            }
                            completion(null, ids.toList())
                        } catch (e: Exception) {
                            print(e)
                            Log.d("retrofit ", "error persisting auto service" + e)
                        }
                    }
                }
            }
        })
        
        return call
    }

    suspend private fun insertNestedAutoService(autoService: AutoService): DataAutoService {
        val storedAutoService = DataAutoService(autoService)
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

class EmptyBodyError(message: String = "The body of the response was empty") : Throwable(message) {}
