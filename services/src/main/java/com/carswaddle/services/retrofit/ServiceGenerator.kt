package com.carswaddle.carswaddleandroid.retrofit

import android.content.Context
import com.carswaddle.services.Authentication
import com.carswaddle.carswaddleandroid.services.serviceModels.CreateServiceEntity
import com.carswaddle.carswaddleandroid.services.serviceModels.CreateServiceEntitySerializer
import com.carswaddle.foundation.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val okHttpClient = OkHttpClient()

private val productionUrl = "https://api.carswaddle.com"
private val stagingUrl = "https://api.staging.carswaddle.com"
private val localUrl = "Kyles-MacBook-Pro.local"

val server: Server = Server.fromBuildConfigs()

enum class Server() {
    staging,
    production,
    local;
    
    companion object {
        fun fromBuildConfigs(): Server {
            when (BuildConfig.BUILD_TYPE) {
                "debug" -> return staging
                "release" -> return production
                else -> {
                    return staging
                }
            }
        }
    }
    
} 

fun serverUrl(): String {
    return when(server) {
        Server.staging -> stagingUrl
        Server.production -> productionUrl
        Server.local -> localUrl
    }
}

val serviceGenerator = ServiceGenerator(serverUrl(), okHttpClient)

class ServiceGenerator(baseURL: String, okHttpClient: OkHttpClient) {

    val retrofit: Retrofit

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(CreateServiceEntity::class.java, CreateServiceEntitySerializer())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
        
        this.retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(EnumConverterFactory())
            .client(okHttpClient)
            .build()
    }

    companion object {

        private var authenticated: ServiceGenerator? = null
        
        public fun reset() {
            authenticated = null
        }
        
        public fun authenticated(context: Context): ServiceGenerator? {
            if (this.authenticated == null) {
                val authToken = Authentication(context).getAuthToken()
                if (authToken != null) {
                    val builder = OkHttpClient.Builder()
                    builder.addInterceptor(AuthenticationInterceptor(authToken))
                    val authenticatedOkHttpClient = builder.build()
                    val s = ServiceGenerator(serverUrl(), authenticatedOkHttpClient)
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


class ServiceNotAvailable(message: String = "Cannot create a Service to make network request") : Throwable(message) {}

class EmptyResponseBody() : Throwable() {}
