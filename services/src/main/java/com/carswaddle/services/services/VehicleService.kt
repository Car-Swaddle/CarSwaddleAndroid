package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Vehicle
import retrofit2.Call
import retrofit2.http.*

private const val vehicles = "/api/vehicles"
private const val vehicle = "/api/vehicle"

interface VehicleService {
    
    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(vehicles)
    fun getVehicles(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<List<Vehicle>>
    
    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(vehicle)
    fun createVehicle(@Body vehicleCreate: VehicleCreate): Call<Vehicle>
    
}

data class VehicleCreate(
    val name: String,
    val licensePlate: String?,
    val vin: String?,
    val state: String
)