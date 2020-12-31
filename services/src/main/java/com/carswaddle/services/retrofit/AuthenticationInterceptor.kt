package com.carswaddle.carswaddleandroid.retrofit

import okhttp3.*
import java.io.IOException

class AuthenticationInterceptor(val authToken: String): Interceptor {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request?.newBuilder()
            ?.addHeader("Authorization", "Bearer " + authToken)
            ?.build()
        return chain.proceed(request)
    }

    /**
     * Authenticator for when the authToken need to be refresh and updated
     * everytime we get a 401 error code
     */
//    @Throws(IOException::class)
//    override fun authenticate (route: Route?, response: Response?): Request? {
//        return null
////        var requestAvailable: Request? = null
////        try {
////            requestAvailable = response?.request()?.newBuilder()
////                ?.addHeader("AUTH_TOKEN", "UUID.randomUUID().toString()")
////                ?.build()
////            return requestAvailable
////        } catch (ex: Exception) { }
////        return requestAvailable
//    }

}
