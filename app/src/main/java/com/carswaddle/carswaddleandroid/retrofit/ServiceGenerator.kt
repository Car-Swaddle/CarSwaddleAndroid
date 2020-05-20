package com.carswaddle.carswaddleandroid.retrofit

import android.app.Service
import android.content.Context
import com.carswaddle.carswaddleandroid.data.Authentication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val okHttpClient = OkHttpClient()


private val production = "https://api.carswaddle.com"

val serviceGenerator = ServiceGenerator(production, okHttpClient)



class ServiceGenerator(baseURL: String, okHttpClient: OkHttpClient) {

    val retrofit: Retrofit

    init {
        this.retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
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
                    val s = ServiceGenerator(production, authenticatedOkHttpClient)
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
