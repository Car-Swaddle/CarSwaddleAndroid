package com.carswaddle.carswaddleandroid.data.mechanic

import android.content.Context
import android.net.Uri
import android.util.Log
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.data.user.ServiceError
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.IdDocumentImageSide
import com.carswaddle.carswaddleandroid.services.MechanicService
import com.carswaddle.carswaddleandroid.services.serviceModels.*
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic as StoreMechanic
import  com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic as ServiceMechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.Verification as ServiceVerification
import com.carswaddle.services.services.StripeService
import com.carswaddle.store.mechanic.Verification as StoreVerification
import com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan as TemplateTimeSpanModel
import com.carswaddle.store.mechanic.OilChangePricing as StoreOilChangePricing
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.net.URI

private val currentMechanicIdKey: String = "com.carswaddle.carswaddleandroid.user.currentMechanicId"

private val mechanicImportQueue = newSingleThreadContext("com.carswaddle.carswaddlemechanic.mechanicImportQueue")


class MechanicRepository(private val mechanicDao: MechanicDao) {

    fun getMechanic(mechanicId: String): com.carswaddle.carswaddleandroid.data.mechanic.Mechanic? {
        return mechanicDao.getMechanic(mechanicId)
    }

    fun getVerification(mechanicId: String): com.carswaddle.store.mechanic.Verification? {
        return mechanicDao.getVerification(mechanicId)
    }

    fun getMechanics(mechanicIds: List<String>): List<com.carswaddle.carswaddleandroid.data.mechanic.Mechanic>? {
        return mechanicDao.getMechanics(mechanicIds)
    }

    fun insertVerification(verification: StoreVerification) {
        return mechanicDao.insertVerification(verification)
    }

    fun getNearestMechanics(
        latitude: Double,
        longitude: Double,
        maxDistance: Double,
        limit: Int,
        context: Context,
        completion: (error: Throwable?, mechanicIDs: List<String>?) -> Unit
    ) {
        val mechanicService =
            ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable("No able to make service to make network call"), null)
            return
        }

        val call = mechanicService.getNearestMechanic(latitude, longitude, maxDistance, limit)
        call.enqueue(object : Callback<List<Map<String, Any>>> {

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<List<Map<String, Any>>>,
                response: Response<List<Map<String, Any>>>
            ) {
                val result = response.body()
                val code = response.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(mechanicImportQueue).launch {
                        try {
                            var ids = arrayListOf<String>()
                            val gson = Gson()
                            for (map in result) {
                                val mechanic = mechanicFromMap(map)
                                insertNestedMechanic(mechanic)
                                ids.add(mechanic.id)
                            }

                            completion(null, ids.toList())
                        } catch (e: Exception) {
                            print(e)
                            Log.d("retrofit ", "error persisting auto service" + e)
                            completion(e, null)
                        }
                    }
                }
            }

        })
    }

    fun getCurrentMechanic(context: Context, completion: (throwable: Throwable?) -> Unit) {
        val mechanicService = ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable("No able to make service to make network call"))
            return
        }

        val call = mechanicService.getCurrentMechanic()
        call.enqueue(object : Callback<Map<String, Any>> {

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                completion(t)
            }

            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                val result = response?.body()
                if (result == null) {
                    completion(Throwable("The result was empty or got invalid response code"))
                } else {
                    CoroutineScope(mechanicImportQueue).launch {
                        try {
                            val mechanic = mechanicFromMap(result)
                            insertNestedMechanic(mechanic)
                            completion(null)
                        } catch (e: Exception) {
                            print(e)
                            Log.d("retrofit ", "error persisting auto service" + e)
                            completion(e)
                        }
                    }
                }
            }

        })
    }


    private fun mechanicFromMap(map: Map<String, Any>): ServiceMechanic {
        val gson = Gson()
        val newMap = map.toMutableMap()
        val jsonTree = gson.toJsonTree(map)
        val user = gson.fromJson<User>(jsonTree, User::class.java)
        val userMap =
            gson.fromJson<Map<String, Any>>(gson.toJsonTree(user), Map::class.java).toMutableMap()
        // The value for `id` is the mechanic id, set `userID` as `id` in newMap 
        (map["userID"] as? String)?.let {
            userMap["id"] = it
        }
        newMap["user"] = userMap
        val newJSONTree = gson.toJsonTree(newMap)
        return gson.fromJson(newJSONTree, ServiceMechanic::class.java)
    }

    fun getStats(
        mechanicId: String,
        context: Context,
        completion: (error: Throwable?, mechanicId: String?) -> Unit
    ) {
        val mechanicService =
            ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable("No able to make service to make network call"), null)
            return
        }

        val call = mechanicService.getStats(mechanicId)
        call.enqueue(object : Callback<Map<String, Any>> {

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                val result = response.body()
                val code = response.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(mechanicImportQueue).launch {
                        var mechanic = mechanicDao.getMechanic(mechanicId)
                        if (mechanic == null) {
                            completion(null, null)
                            return@launch
                        }

                        val gson = Gson()
                        val json = gson.toJsonTree(result[mechanicId])
                        val stats = gson.fromJson(json, Stats::class.java)

                        Log.w("logging stuff", "map: $result")

                        mechanic.averageRating = stats.averageRating
                        mechanic.numberOfRatings = stats.numberOfRatings
                        mechanic.autoServicesProvided = stats.autoServicesProvided

                        mechanicDao.insertMechanic(mechanic)
                        completion(null, mechanicId)
                    }
                }
            }

        })
    }

    fun getTimeSlots(
        mechanicId: String,
        context: Context,
        completion: (error: Throwable?, timeSpanIds: List<String>?) -> Unit
    ): Call<List<TemplateTimeSpanModel>>? {
        val mechanicService =
            ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable("No able to make service to make network call"), null)
            return null
        }

        val call = mechanicService.getAvailability(mechanicId)
        call.enqueue(object : Callback<List<TemplateTimeSpanModel>> {

            override fun onFailure(call: Call<List<TemplateTimeSpanModel>>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<List<TemplateTimeSpanModel>>,
                response: Response<List<TemplateTimeSpanModel>>
            ) {
                val result = response.body()
                val code = response.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(mechanicImportQueue).launch {

                        var spanIds = mutableListOf<String>()

                        for (s in result) {
                            val newSpan = TemplateTimeSpan(s)
                            mechanicDao.insertTimeSpan(newSpan)
                            spanIds.add(newSpan.id)
                        }

                        completion(null, spanIds)
                    }
                }
            }

        })

        return call
    }

    fun updateAvailability(
        updateTimeSpans: List<UpdateTemplateTimeSpan>,
        context: Context,
        completion: (error: Throwable?, timeSpanIds: List<String>?) -> Unit
    ): Call<List<TemplateTimeSpanModel>>? {
        val mechanicService =
            ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable("No able to make service to make network call"), null)
            return null
        }

        val call = mechanicService.updateAvailability(UpdateAvailability(updateTimeSpans))
        call.enqueue(object : Callback<List<TemplateTimeSpanModel>> {

            override fun onFailure(call: Call<List<TemplateTimeSpanModel>>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<List<TemplateTimeSpanModel>>,
                response: Response<List<TemplateTimeSpanModel>>
            ) {
                val result = response.body()
                val code = response.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(mechanicImportQueue).launch {

                        var spanIds = mutableListOf<String>()

                        for (s in result) {
                            val newSpan = TemplateTimeSpan(s)
                            mechanicDao.insertTimeSpan(newSpan)
                            spanIds.add(newSpan.id)
                        }

                        completion(null, spanIds)
                    }
                }
            }

        })

        return call
    }

    fun getOilChangePricing(
        context: Context,
        completion: (error: Throwable?, oilChangePricing: StoreOilChangePricing?) -> Unit
    ): Call<OilChangePricing>? {
        val mechanicService =
            ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            completion(ServiceNotAvailable("No able to make service to make network call"), null)
            return null
        }

        val call = mechanicService.getOilChangePricing()
        call.enqueue(object : Callback<OilChangePricing> {

            override fun onFailure(call: Call<OilChangePricing>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<OilChangePricing>,
                response: Response<OilChangePricing>
            ) {
                val result = response.body()
                val code = response.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val o = StoreOilChangePricing(result)
                        completion(null, o)
                    }
                }
            }

        })

        return call
    }

    fun updateOilChangePricing(
        updateOilChangePricing: UpdateOilChangePricing,
        context: Context,
        completion: (error: Throwable?, oilChangePricing: StoreOilChangePricing?) -> Unit
    ): Call<OilChangePricing>? {
        val mechanicService =
            ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            completion(ServiceNotAvailable("No able to make service to make network call"), null)
            return null
        }

        val call = mechanicService.updateOilchangePricing(updateOilChangePricing)
        call.enqueue(object : Callback<OilChangePricing> {

            override fun onFailure(call: Call<OilChangePricing>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<OilChangePricing>,
                response: Response<OilChangePricing>
            ) {
                val result = response.body()
                val code = response.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val o = StoreOilChangePricing(result)
                        completion(null, o)
                    }
                }
            }

        })

        return call
    }


    private fun insertNestedMechanic(mechanic: ServiceMechanic): MechanicListElements? {
        var storedUser = mechanic.user?.let { User(it) }
        val userId = mechanic.userID
        if (storedUser == null && userId != null) {
            storedUser = mechanicDao.getUser(userId)
        } else if (storedUser != null) {
            mechanicDao.insertUser(storedUser)
        }
        
        val existingMechanic = getMechanic(mechanic.id)
        
        val storedMechanic = StoreMechanic(mechanic, existingMechanic?.averageRating, existingMechanic?.numberOfRatings, existingMechanic?.autoServicesProvided)
        mechanicDao.insertMechanic(storedMechanic)

        var storedTimeSpans = arrayListOf<TemplateTimeSpan>()
        mechanic.scheduleTimeSpans.let {
            if (it != null) {
                for (t in it) {
                    val storedTimeSpan = TemplateTimeSpan(t)
                    storedTimeSpans.add(storedTimeSpan)
                    mechanicDao.insertTimeSpan(storedTimeSpan)
                }
            }
        }
        
        print("mechanic inserted $storedMechanic")
        Log.w("mechanic insertion", "mechanic inserted $storedMechanic")

        if (storedUser != null) {
            return MechanicListElements(storedMechanic, storedUser, storedTimeSpans)
        } else {
            return null
        }
    }

    fun updateMechanic(
        updateMechanic: UpdateMechanic,
        context: Context,
        cacheCompletion: () -> Unit = {},
        completion: (throwable: Throwable?) -> Unit
    ) {
        val mechanicService = ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            completion(ServiceNotAvailable())
            return
        }

        val call = mechanicService.updateMechanic(updateMechanic)
        call.enqueue(object : Callback<ServiceMechanic> {
            override fun onFailure(call: Call<ServiceMechanic>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(Error(t.localizedMessage))
            }

            override fun onResponse(
                call: Call<ServiceMechanic>,
                response: Response<ServiceMechanic>
            ) {
                Log.d("retrofit ", "call succeeded")
                val result = response.body()
                if (result == null) {
                    Log.d("retrofit ", "call failed")
                    completion(ServiceError())
                } else {
                    Log.d("retrofit ", "call succeeded")
                    CoroutineScope(mechanicImportQueue).launch {
                        insertNestedMechanic(result)
                        completion(null)
                    }
                }
            }
        })
    }
    
    fun uploadIdDocument(fileUri: String, side: IdDocumentImageSide, context: Context, completion: (throwable: Throwable?) -> Unit) {
        val mechanicService = ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            completion(ServiceNotAvailable())
            return
        }
        
        val file = File(fileUri)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val filePart = MultipartBody.Part.createFormData("image", file.name, requestBody)
        val call = mechanicService.uploadIdDocument(filePart, side)
        call.enqueue(object : Callback<ServiceMechanic?> {
            override fun onFailure(call: Call<ServiceMechanic?>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(Error(t.localizedMessage))
            }
            
            override fun onResponse(
                call: Call<ServiceMechanic?>,
                response: Response<ServiceMechanic?>
            ) {
                Log.d("retrofit ", "call succeeded")
                val result = response.body()
                if (result == null) {
                    Log.d("retrofit ", "call failed")
                    completion(ServiceError())
                } else {
                    Log.d("retrofit ", "call succeeded")
                    CoroutineScope(mechanicImportQueue).launch {
                        insertNestedMechanic(result)
                        completion(null)
                    }
                }
            }
        })
    }

    fun updatVerification(context: Context, completion: (throwable: Throwable?) -> Unit) {
        val stripeService = ServiceGenerator.authenticated(context)?.retrofit?.create(StripeService::class.java)
        if (stripeService == null) {
            completion(ServiceNotAvailable())
            return
        }

        val call = stripeService.getVerification()
        call.enqueue(object : Callback<ServiceVerification> {
            override fun onFailure(call: Call<ServiceVerification>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(Error(t.localizedMessage))
            }

            override fun onResponse(
                call: Call<ServiceVerification>,
                response: Response<ServiceVerification>
            ) {
                Log.d("retrofit ", "call succeeded")
                val result = response.body()
                if (result == null) {
                    // TODO: make an error here
                    Log.d("retrofit ", "call failed")
                    completion(ServiceError())
                } else {
                    Log.d("retrofit ", "call succeeded")
                    CoroutineScope(mechanicImportQueue).launch {
                        val mechanicId = getCurrentMechanicId(context) ?: ""
                        insertVerification(StoreVerification(result, mechanicId))
                        completion(null)
                    }
                }
            }
        })
    }

    fun getCurrentMechanicVerification(context: Context): StoreVerification? {
        val mechanicId = getCurrentMechanicId(context) ?: return null
        return mechanicDao.getVerification(mechanicId)
    }

    fun getCurrentMechanic(context: Context): com.carswaddle.carswaddleandroid.data.mechanic.Mechanic? {
        val mechanicId = getCurrentMechanicId(context) ?: return null
        return mechanicDao.getMechanic(mechanicId)
    }

    fun getCurrentMechanicId(context: Context): String? {
        val preferences = context.carSwaddlePreferences()
        return preferences.getString(currentMechanicIdKey, null)
    }

    fun setCurrentMechanicId(userId: String, context: Context) {
        val editContext = context.carSwaddlePreferences().edit()
        editContext.putString(currentMechanicIdKey, userId)
        editContext.apply()
    }

}


data class MechanicListElements(
    val mechanic: com.carswaddle.carswaddleandroid.data.mechanic.Mechanic,
    val user: User,
    val timeSpans: List<TemplateTimeSpan>
)


