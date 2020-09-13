package com.carswaddle.carswaddleandroid.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository

class ForgotPasswordActivity: AppCompatActivity() {

    private val emailEditText: EditText by lazy { findViewById(R.id.email_edit_text) as EditText }
    private val sendButton: Button by lazy { findViewById(R.id.resendCodeButton) as Button }

    private lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        sendButton.setOnClickListener {
            sendResetLink()
        }
    }

    private fun sendResetLink() {

    }

}