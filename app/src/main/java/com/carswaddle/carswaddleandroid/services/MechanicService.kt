package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.*
import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.Map


private const val nearestMechanicEndpoint = "/api/nearest-mechanics"
private const val statsEndpoint = "/api/stats"
private const val availabilityEndpoint = "/api/availability"

interface MechanicService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(nearestMechanicEndpoint)
    fun getNearestMechanic(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("maxDistance") maxDistance: Double, @Query("limit") Int: Int): Call<List<Map<String, Any>>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(statsEndpoint)
    fun getStats(@Query("mechanic") mechanic: String): Call<Map<String, Any>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(availabilityEndpoint)
    fun getAvailability(@Query("mechanicID") mechanicId: String): Call<List<TemplateTimeSpan>>

}
