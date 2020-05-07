package com.carswaddle.carswaddleandroid.services

import retrofit2.Call
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import retrofit2.http.*


interface AuthenticationService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("email") email: String?, @Field("password") password: String, @Query("isMechanic") isMechanic: Boolean): Call<AuthResponse>

}
