package com.carswaddle.carswaddleandroid.data.mechanic

import android.content.Context
import android.util.Log
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.MechanicService
//import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.Stats
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.protobuf.Parser
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MechanicRepository(private val mechanicDao: MechanicDao) {

    fun getMechanic(mechanicId: String): com.carswaddle.carswaddleandroid.data.mechanic.Mechanic? {
        return mechanicDao.getMechanic(mechanicId)
    }

    fun getMechanics(mechanicIds: List<String>): List<com.carswaddle.carswaddleandroid.data.mechanic.Mechanic>? {
        return mechanicDao.getMechanics(mechanicIds)
    }

    fun getNearestMechanics(latitude: Double, longitude: Double, maxDistance: Double, limit: Int, context: Context, completion: (error: Throwable?, mechanicIDs: List<String>?) -> Unit) {
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
                val result = response?.body()
                val code = response?.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var ids = arrayListOf<String>()
                            val gson = Gson()
                            for (map in result) {
                                var newMap = map.toMutableMap() 
                                val jsonTree = gson.toJsonTree(map)
                                val user = gson.fromJson<User>(jsonTree, User::class.java)
                                val userMap = gson.fromJson<Map<String, Any>>(gson.toJsonTree(user), Map::class.java)
                                newMap["user"] = userMap
                                val newJSONTree = gson.toJsonTree(newMap)
                                val mechanic = gson.fromJson<Mechanic>(newJSONTree, Mechanic::class.java)
                                insertNestedMechanic(mechanic)
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

    fun getStats(mechanicId: String, context: Context, completion: (error: Throwable?, mechanicId: String?) -> Unit) {
        val mechanicService = ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable("No able to make service to make network call"), null)
            return
        }

        val call = mechanicService.getStats(mechanicId)
        call.enqueue(object :
            Callback<Stats> {
            override fun onFailure(call: Call<Stats>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<Stats>,
                response: Response<Stats>
            ) {
                val result = response?.body()
                val code = response?.code()
                if (code < 200 || code >= 300 || result == null) {
                    completion(Throwable("The result was empty or got invalid response code"), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        var mechanic = mechanicDao.getMechanic(mechanicId)
                        if (mechanic == null) {
                            return@launch
                        }

                        mechanic.averageRating = result.averageRating
                        mechanic.numberOfRatings = result.numberOfRatings
                        mechanic.autoServicesProvided = result.autoServicesProvided

                        mechanicDao.insertMechanic(mechanic)
                        completion(null, mechanicId)
                    }
                }
            }

        })
    }


    suspend private fun insertNestedMechanic(mechanic: com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic): MechanicListElements? {
        var storedUser = mechanic.user?.let { User(it) }
        val userId = mechanic.userID
        if (storedUser == null && userId != null) {
            storedUser = mechanicDao.getUser(userId)
        } else if (storedUser != null) {
            mechanicDao.insertUser(storedUser)
        }
        
        val storedMechanic = Mechanic(mechanic)
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

        if (storedUser != null) {
            return MechanicListElements(storedMechanic, storedUser, storedTimeSpans)
        } else {
            return null
        }
        
    }


}


data class MechanicListElements(
    val mechanic: com.carswaddle.carswaddleandroid.data.mechanic.Mechanic,
    val user: com.carswaddle.carswaddleandroid.data.user.User,
    val timeSpans: List<com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan>
)