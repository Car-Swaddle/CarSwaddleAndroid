package com.carswaddle.carswaddleandroid.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


private const val mechanicProfileImage = "/api/data/mechanic/profile-picture/{mechanicId}"

interface FileService {

    @Headers(ContentType.headerPrefix + ContentType.imagePNG)
    @GET(mechanicProfileImage)
    fun getStats(@Query("mechanicId") mechanicId: String): Call<ResponseBody>
    
}
