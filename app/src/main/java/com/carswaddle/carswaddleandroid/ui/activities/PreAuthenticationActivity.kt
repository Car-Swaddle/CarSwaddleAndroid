package com.carswaddle.carswaddleandroid.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.activities.ui.LoginActivity
import com.carswaddle.carswaddleandroid.R.layout.activity_pre_auth

class PreAuthenticationActivity: AppCompatActivity() {

    private val loginButton: Button by lazy { findViewById(R.id.loginButton) as Button }
    private val signUpButton: Button by lazy { findViewById(R.id.signUpButton) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_pre_auth)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

}