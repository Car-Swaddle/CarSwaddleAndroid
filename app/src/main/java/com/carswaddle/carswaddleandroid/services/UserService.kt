package com.carswaddle.carswaddleandroid.services

import retrofit2.Call
import com.carswaddle.carswaddleandroid.services.serviceModels.User
import retrofit2.http.*


interface UserService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET("/api/current-user")
    @Authenticated
    fun currentUser(): Call<User>

}


annotation class Authenticated()
