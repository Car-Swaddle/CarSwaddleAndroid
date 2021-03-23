package com.carswaddle.carswaddleandroid.activities.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.CarSwaddleApp.CarSwaddleApp
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.pushNotifications.MessagingController
import com.carswaddle.carswaddleandroid.ui.activities.PreAuthenticationActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetNameActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetPhoneNumberActivity
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase


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
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData -> 
                if (pendingDynamicLinkData != null) { 
                    val link = pendingDynamicLinkData.link
                    link?.getQueryParameter("referrerId").let {
                        val p = carSwaddlePreferences().edit()
                            p.putString("referrerId", it)
                            p.apply()
                        }
                    }
                    Log.w("This is a tag", "test")
                    runOnUiThread {
                        navigateToActivity() 
                    } 
            }
                .addOnFailureListener(this) { e ->
                    Log.w("This is a tag", "getDynamicLink:onFailure", e)
                }
    }
    
    private fun navigateToActivity() {
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
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.putExtras(intent)
                    startActivity(mainIntent)
                }
                val p = carSwaddlePreferences()
                val referrerId = p.getString("referrerId", null)
                if (referrerId != null) {
                    userRepo.updateReferrerId(referrerId, this, {}) {
                        Log.w("tag", "got back")
                    }
                    val edit = p.edit()
                    edit.putString("referrerId", null)
                    edit.apply()
                }
            }

            MessagingController.instance.registerPushToken()
        } else {
            val intent = Intent(this, PreAuthenticationActivity::class.java)
            startActivity(intent)
        }
    }

}