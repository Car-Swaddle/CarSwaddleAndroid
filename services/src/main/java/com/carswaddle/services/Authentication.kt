package com.carswaddle.services

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import kotlinx.coroutines.*

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

    fun logout(completion: (error: Throwable?, response: AuthResponse?) -> Unit) {

        val intent = Intent(USER_WILL_LOGOUT)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        
        removeToken()
        // TODO: make network request to remove push tokens and auth token from server

        CoroutineScope(Dispatchers.Default).launch {
            val intent = Intent(USER_DID_LOGOUT)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        completion(null, null)
    }

    private fun preferences(): SharedPreferences {
        return context.carSwaddlePreferences()
    }
    
    companion object {
        
        const val USER_DID_LOGOUT = "Authentication.USER_DID_LOGOUT"
        const val USER_WILL_LOGOUT = "Authentication.USER_WILL_LOGOUT"
        
    }

}
