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
private const val mechanicPricing = "/api/mechanic/pricing"
private const val mechanicRegion = "/api/region"
private const val reviews = "/api/reviews"

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
    @POST(availabilityEndpoint)
    fun updateAvailability(@Body body: UpdateAvailability): Call<List<TemplateTimeSpan>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @PATCH(updateMechanic)
    fun updateMechanic(@Body updateMechanic: UpdateMechanic): Call<Mechanic>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @PATCH(updateMechanic)
    fun getCurrentMechanic(): Call<Map<String, Any>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(mechanicPricing)
    fun getOilChangePricing(): Call<OilChangePricing>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @PUT(mechanicPricing)
    fun updateOilchangePricing(@Body pricing: UpdateOilChangePricing): Call<OilChangePricing>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(mechanicRegion)
    fun getMechanicRegion(): Call<Region>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(mechanicRegion)
    fun updateMechanicRegion(updateRegion: UpdateRegion): Call<Region>

    /// If 'mechanic' is null, returns reviews given by current user
    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(reviews)
    fun getReviews(@Query("mechanic") mechanicId: String?, @Query("limit") limit: Int = 100, @Query("offset") offset: Int): Call<ReviewResponse>
    
}
