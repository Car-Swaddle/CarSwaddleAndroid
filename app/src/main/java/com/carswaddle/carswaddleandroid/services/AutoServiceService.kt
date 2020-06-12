package com.carswaddle.carswaddleandroid.services

import retrofit2.Call
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import retrofit2.http.*

private const val autoServiceEndpoint = "/api/auto-service"

interface AutoServiceService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(autoServiceEndpoint)
    fun autoServices(@Query("limit") limit: Int, @Query("offset") offset: Int, @Query("sortStatus") sortStatus: Array<String>): Call<Array<AutoService>>

}
