package com.carswaddle.carswaddleandroid.activities.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.pushNotifications.MessagingController
import com.carswaddle.carswaddleandroid.ui.activities.PreAuthenticationActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetNameActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetPhoneNumberActivity


class SplashActivity: AppCompatActivity() {

    private lateinit var userRepo: UserRepository

    private var auth = Authentication(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        userRepo = UserRepository(db.userDao())

        setContentView(R.layout.splash)

        // Create here so it can listen if the user logs in
        MessagingController.initialize()
        
        if (auth.isUserLoggedIn()) {
            userRepo.importCurrentUser(this) {
                val user = userRepo.getCurrentUser(this)
                if (it != null) {
                    val intent = Intent(this, PreAuthenticationActivity::class.java)
                    startActivity(intent)
                } else if (user == null) {
                    val intent = Intent(this, PreAuthenticationActivity::class.java)
                    startActivity(intent)
                } else if (user.firstName.isNullOrBlank() || user.lastName.isNullOrBlank()) {
                    val intent = Intent(this, SetNameActivity::class.java)
                    startActivity(intent)
                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == null || user.isPhoneNumberVerified == false) {
                    val intent = Intent(this, SetPhoneNumberActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            
            MessagingController.instance.registerPushToken()
        } else {
            val intent = Intent(this, PreAuthenticationActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

}