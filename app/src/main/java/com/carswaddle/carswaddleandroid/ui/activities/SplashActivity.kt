package com.carswaddle.carswaddleandroid.activities.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.Authentication


class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.splash)

        if (auth.isUserLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private var auth = Authentication(this)

}