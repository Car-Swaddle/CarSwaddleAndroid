package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateUser
import retrofit2.Call
import com.carswaddle.carswaddleandroid.services.serviceModels.User
import retrofit2.http.*


interface UserService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET("/api/current-user")
    fun currentUser(): Call<User>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @PATCH("/api/update-user")
    fun updateUser(@Body updateUser: UpdateUser): Call<User>

}
