package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.Stats
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateAutoService
import retrofit2.Call
import retrofit2.http.*


private const val nearestMechanicEndpoint = "/api/nearest-mechanics"
private const val statsEndpoint = "/api/stats"

interface MechanicService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(nearestMechanicEndpoint)
    fun getNearestMechanic(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("maxDistance") maxDistance: Double, @Query("limit") Int: Int): Call<List<Mechanic>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(statsEndpoint)
    fun getStats(@Query("mechanic") mechanic: String): Call<Stats>

}