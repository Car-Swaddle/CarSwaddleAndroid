package com.carswaddle.carswaddleandroid.activities.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.CarSwaddleApp.CarSwaddleApp
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.messaging.Intercom
import com.carswaddle.carswaddleandroid.messaging.MessagingController
import com.carswaddle.carswaddleandroid.ui.activities.PreAuthenticationActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetNameActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetPhoneNumberActivity
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.util.LinkProperties
import org.json.JSONObject

// intent.data.host == "go.carswaddle.com"

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
        
        navigateToActivity()
    }

    override fun onStart() {
        super.onStart()
        
        Intercom.shared.setup(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        // Branch reinit (in case Activity is already in foreground when Branch link is clicked)
//        Branch.sessionBuilder(this).withCallback(branchListener).reInit()
        Intercom.shared.setupFromNewIntent(this)
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
            }

            MessagingController.instance.registerPushToken()
        } else {
            val intent = Intent(this, PreAuthenticationActivity::class.java)
            startActivity(intent)
        }
    }

}