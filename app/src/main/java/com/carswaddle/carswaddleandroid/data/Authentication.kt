package com.carswaddle.carswaddleandroid.data

import android.content.Context
import android.content.SharedPreferences

private val authSharedPreferencesName = "authSharedPreferencesName"
private val authTokenKey = "authTokenKey"

class Authentication(context: Context) {

    private val context: Context = context

    fun isUserLoggedIn(): Boolean {
        return preferences().getString(authTokenKey, null) != null
    }

    fun setLoginToken(token: String) {
        val preferences = context.getSharedPreferences(authSharedPreferencesName, Context.MODE_PRIVATE)
        val editContext = preferences().edit()
        editContext.putString(authTokenKey, token)
    }

    private fun preferences(): SharedPreferences {
        return context.getSharedPreferences(authSharedPreferencesName, Context.MODE_PRIVATE)
    }

}