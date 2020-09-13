package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.ContentType.Companion.headerPrefix
import com.carswaddle.carswaddleandroid.services.ContentType.Companion.applicationJSON
import com.carswaddle.carswaddleandroid.services.ContentType.Companion.applicationFormURLEncoded
import retrofit2.Call
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import com.carswaddle.carswaddleandroid.services.serviceModels.User
import retrofit2.http.*


interface AuthenticationService {

    @Headers(headerPrefix + applicationFormURLEncoded)
    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("email") email: String?, @Field("password") password: String, @Query("isMechanic") isMechanic: Boolean): Call<AuthResponse>

    @Headers(headerPrefix + applicationJSON)
    @POST("/api/logout")
    fun logout(@Body deviceToken: String): Call<AuthResponse>

    @Headers(headerPrefix + applicationFormURLEncoded)
    @FormUrlEncoded
    @POST("/signup")
    fun signUp(@Field("email") email: String?, @Field("password") password: String, @Query("isMechanic") isMechanic: Boolean): Call<AuthResponse>

    @Headers(headerPrefix + applicationJSON)
    @GET("/api/sms/verify")
    fun verifySMS(@Query("code") code: String): Call<User>

    @Headers(headerPrefix + applicationJSON)
    @GET("/api/sms/send-verification")
    fun sendSMSVerification(): Call<Void>

    @Headers(headerPrefix + applicationJSON)
    @GET("/api/reset-password")
    fun resetPassword(): Call<Void>

    @Headers(headerPrefix + applicationJSON)
    @GET("/api/request-reset-password")
    fun requestResetPasswordLink(): Call<Void>

    @Headers(headerPrefix + applicationFormURLEncoded)
    @FormUrlEncoded
    @POST("/api/request-reset-password")
    fun signUp(@Field("email") email: String?, @Field("appName") appName: String): Call<AuthResponse>

}
