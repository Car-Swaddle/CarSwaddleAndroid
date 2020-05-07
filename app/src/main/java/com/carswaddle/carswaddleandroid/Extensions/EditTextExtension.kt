package com.carswaddle.carswaddleandroid.Extensions

import android.widget.EditText

fun EditText.isEmpty(): Boolean {
    return text.trim().isEmpty()
}