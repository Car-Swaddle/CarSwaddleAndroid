package com.carswaddle.services.services

import com.carswaddle.carswaddleandroid.services.ContentType
import com.carswaddle.carswaddleandroid.services.PriceRequest
import com.carswaddle.carswaddleandroid.services.serviceModels.Region
import com.carswaddle.carswaddleandroid.services.serviceModels.TaxInfo
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateRegion
import retrofit2.Call
import retrofit2.http.*


private const val regionEndpoint = "api/region"

interface RegionService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(regionEndpoint)
    fun getRegion(): Call<Region>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(regionEndpoint)
    fun updateRegion(@Body updateregion: UpdateRegion): Call<Region>

}
