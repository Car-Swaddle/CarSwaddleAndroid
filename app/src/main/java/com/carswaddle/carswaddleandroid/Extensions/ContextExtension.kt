package com.carswaddle.carswaddleandroid.Extensions

import android.content.Context
import android.content.SharedPreferences
import com.carswaddle.carswaddleandroid.R

fun Context.carSwaddlePreferences(): SharedPreferences {
    return getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
}