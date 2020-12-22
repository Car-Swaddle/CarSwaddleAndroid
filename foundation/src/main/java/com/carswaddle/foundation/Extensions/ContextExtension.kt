package com.carswaddle.carswaddleandroid.Extensions

import android.content.Context
import android.content.SharedPreferences

private val preferencesPath = "com.carswaddle.carswaddleandroid.preferences"

fun Context.carSwaddlePreferences(): SharedPreferences {
    return getSharedPreferences(preferencesPath, Context.MODE_PRIVATE)
}