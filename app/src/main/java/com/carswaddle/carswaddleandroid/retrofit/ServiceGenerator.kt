package com.carswaddle.carswaddleandroid.retrofit

import android.content.Context
import com.carswaddle.carswaddleandroid.data.Authentication
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val okHttpClient = OkHttpClient()

private val production = "https://api.carswaddle.com"
private val staging = "https://api.staging.carswaddle.com"

val serviceGenerator = ServiceGenerator(staging, okHttpClient)

class ServiceGenerator(baseURL: String, okHttpClient: OkHttpClient) {

    val retrofit: Retrofit

    init {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
        this.retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(EnumConverterFactory())
            .client(okHttpClient)
            .build()
    }

    companion object {

        private var authenticated: ServiceGenerator? = null

        public fun authenticated(context: Context): ServiceGenerator? {
            if (this.authenticated == null) {
                val authToken = Authentication(context).getAuthToken()
                if (authToken != null) {
                    val builder = OkHttpClient.Builder()
                    builder.addInterceptor(AuthenticationInterceptor(authToken))
                    val authenticatedOkHttpClient = builder.build()
                    val s = ServiceGenerator(staging, authenticatedOkHttpClient)
                    this.authenticated = s
                    return s
                } else {
                    return null
                }
            } else {
                return this.authenticated
            }
        }

    }

}


class ServiceNotAvailable(message: String) : Throwable(message) {}

