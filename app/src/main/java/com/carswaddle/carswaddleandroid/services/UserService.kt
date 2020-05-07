package com.carswaddle.carswaddleandroid.services

import retrofit2.Call
import com.carswaddle.carswaddleandroid.services.serviceModels.User
import retrofit2.http.*


interface UserService {

    @Headers("Content-Type: application/json")
    @GET("/api/current-user")
    fun currentUser(): Call<User>

}
