package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Vehicle
import retrofit2.Call
import retrofit2.http.*

private const val vehicles = "/api/vehicles"

interface VehicleService {
    
    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(vehicles)
    fun getVehicles(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<List<Vehicle>>
    
    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(vehicles)
    fun createVehicle(@Field("name") name: String, 
                      @Field("licensePlate") licensePlate: String,
                      @Field("vin") vin: String,
                      @Field("state") state: String): Call<Vehicle>
    
}
