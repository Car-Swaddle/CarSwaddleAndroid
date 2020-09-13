package com.carswaddle.carswaddleandroid.ui.activities.resetPassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resetToken = intent?.data?.getQueryParameter("resetToken")
        if (resetToken != null) {

        }
    }

}