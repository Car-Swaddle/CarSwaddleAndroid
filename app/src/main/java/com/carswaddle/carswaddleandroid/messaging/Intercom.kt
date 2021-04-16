package com.carswaddle.carswaddleandroid.messaging

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.activities.ui.SplashActivity
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject

private val affiliateDomain = "affiliate.carswaddle.com"
private val goDomain = "go.carswaddle.com"
private val appDomain = "app.carswaddle.com"

class Intercom {
    
    private val referrerIdKey = "referrerIdKey"
    private val userRepo: UserRepository

    private val auth: Authentication
    
    private val context: Context
    
    constructor(context: Context) {
        this.context = context
        val db = AppDatabase.getDatabase(context)
        userRepo = UserRepository(db.userDao())
        auth = Authentication(context)
    }
    
    fun setup(activity: Activity) {
        Branch.sessionBuilder(activity).withCallback(branchListener).withData(activity.intent?.data).init()
    }
    
    fun setupFromNewIntent(activity: Activity) {
        Branch.sessionBuilder(activity).withCallback(branchListener).reInit()
    }

    private object branchListener : Branch.BranchReferralInitListener {
        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                val referrerId = referringParams?.get("referrerId")
                if (referrerId != null && referrerId is String) {
                    shared.updateForReferrerId(referrerId)
                }
            } else {
                Log.e("BRANCH SDK", error.message)
            }
        }
    }
    
    private fun updateForReferrerId(newReferrerId: String) {
        if (auth.isUserLoggedIn()) {
            userRepo.updateReferrerId(newReferrerId, context, {}) {
                if (it != null) {
                    Log.w("Intercom", "error update user with referrer")
                }
            }
        } else {
            _referrerId = newReferrerId
        }
    }
    
    private var _referrerId: String?
        get() {
            return context.carSwaddlePreferences().getString(referrerIdKey, null)
        }
        set(newValue) {
            val edit = context.carSwaddlePreferences().edit()
            edit.putString(referrerIdKey, newValue)
            edit.apply()
        }

    var referrerId: String? = null
        get() = _referrerId
    
    fun wipeReferrerId() {
        _referrerId = null
    }
    
    companion object {
        lateinit var shared: Intercom
    }
    
}
