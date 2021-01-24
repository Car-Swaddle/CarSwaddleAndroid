package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.*
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.Map


private const val nearestMechanicEndpoint = "/api/nearest-mechanics"
private const val statsEndpoint = "/api/stats"
private const val availabilityEndpoint = "/api/availability"
private const val updateMechanic = "api/update-mechanic"
private const val currentMechanic = "/api/current-mechanic"

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

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @PATCH(updateMechanic)
    fun updateMechanic(@Body updateMechanic: UpdateMechanic): Call<Mechanic>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @PATCH(updateMechanic)
    fun getCurrentMechanic(): Call<Map<String, Any>>
    
}

data class UpdateMechanic(
    val isActive: Boolean? = null, // Allow new appointments
    val token: String? = null,
    val dateOfBirth: Date? = null,
    val address: UpdateMechanicAddress? = null,
    val externalAccount: String? = null,
    val ssnLast4: String? = null,
    val personalID: String? = null,
    val chargeForTravel: Boolean? = null
)

data class UpdateMechanicAddress(
    val line1: String,
    val line2: String?,
    val postalCode: String,
    val city: String,
    val state: String,
    val country: String
)
