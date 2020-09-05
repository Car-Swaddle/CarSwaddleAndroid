package com.carswaddle.carswaddleandroid.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.retrofit.serviceGenerator
import com.carswaddle.carswaddleandroid.services.AuthenticationService
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val authSharedPreferencesName = "authSharedPreferencesName"
private val authTokenKey = "authTokenKey"

class Authentication(private val context: Context) {


    private val userRepo: UserRepository

    init {
        val db = AppDatabase.getDatabase(context)
        userRepo = UserRepository(db.userDao())
    }



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
                val user = result?.user
                if (user != null) {
                    GlobalScope.async {
                        userRepo.insert(User(user))
                        completion(null, result)
                    }
                } else {
                    completion(null, result)
                }
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

    fun sendSMSVerificationSMS(completion: (error: Throwable?) -> Unit) {
        val auth = serviceGenerator.retrofit.create(AuthenticationService::class.java)
        val call = auth.sendSMSVerification()
        call.enqueue(object : Callback<com.carswaddle.carswaddleandroid.services.serviceModels.User> {
            override fun onFailure(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t)
            }

            override fun onResponse(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>?, response: Response<com.carswaddle.carswaddleandroid.services.serviceModels.User>?) {
                Log.d("retrofit ", "call succeeded")
                val user = response?.body()
                if (user != null) {
                    Log.d("retrofit ", "call succeeded")
                    GlobalScope.async {
                        val dataUser = User(user)
                        userRepo.insert(dataUser)
                        completion(null)
                    }
                }
                completion(null)
            }
        })
    }

    fun verifySMSCode(code: String, completion: (error: Throwable?) -> Unit) {
        val auth = serviceGenerator.retrofit.create(AuthenticationService::class.java)
        val call = auth.verifySMS(code)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t)
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                Log.d("retrofit ", "call succeeded")
                completion(null)
            }
        })
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
