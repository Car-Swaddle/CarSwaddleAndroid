package com.carswaddle.carswaddlemechanic.ui.startup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddlemechanic.MainActivity
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.login.AuthActivity
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase

class SplashActivity: AppCompatActivity() {

    private lateinit var userRepo: UserRepository

    private var auth = Authentication(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        userRepo = UserRepository(db.userDao())

        setContentView(R.layout.activity_splash)

        // Create here so it can listen if the user logs in
//        MessagingController.initialize()

        if (auth.isUserLoggedIn()) {
            userRepo.importCurrentUser(this) {
                val user = userRepo.getCurrentUser(this)
//                if (it != null) {
//                    val intent = Intent(this, PreAuthenticationActivity::class.java)
//                    startActivity(intent)
//                } else if (user == null) {
//                    val intent = Intent(this, PreAuthenticationActivity::class.java)
//                    startActivity(intent)
//                } else if (user.firstName.isNullOrBlank() || user.lastName.isNullOrBlank()) {
//                    val intent = Intent(this, SetNameActivity::class.java)
//                    startActivity(intent)
//                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == null || user.isPhoneNumberVerified == false) {
//                    val intent = Intent(this, SetPhoneNumberActivity::class.java)
//                    startActivity(intent)
//                } else {
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.putExtras(intent)
                    startActivity(mainIntent)
                    finish()
//                }
            }

//            MessagingController.instance.registerPushToken()
        } else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

}