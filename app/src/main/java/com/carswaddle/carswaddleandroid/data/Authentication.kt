package com.carswaddle.carswaddleandroid.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.carswaddle.carswaddleandroid.retrofit.serviceGenerator
import com.carswaddle.carswaddleandroid.services.AuthenticationService
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val authSharedPreferencesName = "authSharedPreferencesName"
private val authTokenKey = "authTokenKey"

class Authentication(context: Context) {

    private val context: Context = context

    fun isUserLoggedIn(): Boolean {
        return preferences().getString(authTokenKey, null) != null
    }

    fun logout() {
        val editContext = preferences().edit()
        editContext.putString(authTokenKey, null)
        editContext.commit()
    }

    fun setLoginToken(token: String) {
        val editContext = preferences().edit()
        editContext.putString(authTokenKey, token)
        editContext.commit()
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

    private fun preferences(): SharedPreferences {
        return context.getSharedPreferences(authSharedPreferencesName, Context.MODE_PRIVATE)
    }

    private fun editContext(): SharedPreferences.Editor {
        return preferences().edit()
    }

}
