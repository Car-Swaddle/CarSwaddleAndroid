package com.carswaddle.carswaddleandroid.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.retrofit.serviceGenerator
import com.carswaddle.carswaddleandroid.services.AuthenticationService
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val authSharedPreferencesName = "authSharedPreferencesName"
private val authTokenKey = "authTokenKey"

class Authentication(private val context: Context) {

    fun isUserLoggedIn(): Boolean {
        return getAuthToken() != null
    }

    fun getAuthToken(): String? {
        return preferences().getString(authTokenKey, null)
    }

    fun setLoginToken(token: String) {
        val editContext = preferences().edit()
        editContext.putString(authTokenKey, token)
        editContext.apply()

    }

    private fun removeToken() {
        val editContext = preferences().edit()
        editContext.putString(authTokenKey, null)
        editContext.apply()
    }

    fun login(email: String, password: String, completion: (error: Throwable?, response: AuthResponse?) -> Unit) {
        val auth = serviceGenerator.retrofit.create(AuthenticationService::class.java)
        val call = auth.login(email, password, false)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result?.token != null) {
                    setLoginToken(result.token)
                }
                completion(null, result)
            }

        })
    }

    fun signUp(email: String, password: String, completion: (error: Throwable?, response: AuthResponse?) -> Unit) {
        val auth = serviceGenerator.retrofit.create(AuthenticationService::class.java)
        val call = auth.signUp(email, password, false)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result?.token != null) {
                    setLoginToken(result.token)
                }
                completion(null, result)
            }
        })
    }

    fun sendSMSVerificationSMS() {

    }

    fun logout(completion: (error: Throwable?, response: AuthResponse?) -> Unit) {
        removeToken()
        // TODO: make network request to remove push tokens and auth token from server
        completion(null, null)
    }

    private fun preferences(): SharedPreferences {
        return context.carSwaddlePreferences()
    }

}
