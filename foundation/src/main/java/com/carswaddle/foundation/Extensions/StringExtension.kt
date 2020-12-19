package com.carswaddle.carswaddleandroid.Extensions

import android.text.TextUtils
import android.widget.EditText


fun String.isEmpty(): Boolean {
    return length > 0
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

