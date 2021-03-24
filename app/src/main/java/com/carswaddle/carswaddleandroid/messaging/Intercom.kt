package com.carswaddle.carswaddleandroid.messaging

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

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
    
    fun handleDynamicLink(intent: Intent, activity: Activity, completion: (error: Throwable?) -> Unit) {
        fetchDynamicLink(intent, activity) { error, dynamicLink ->
            dynamicLink?.let { 
                if (auth.isUserLoggedIn()) {
                    userRepo.updateReferrerId(it, context, {}) {
                        if (it != null) {
                            Log.w("Intercom", "error update user with referrer")
                        }
                    }
                } else {
                    _referrerId = it
                }
            }
        }
    }
    
    /// This just fetches the dynamic link
    private fun fetchDynamicLink(intent: Intent, activity: Activity, completion: (error: Throwable?, dynamicLink: String?) -> Unit) {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(activity) { pendingDynamicLinkData ->
                var d: String?
                pendingDynamicLinkData.link?.getQueryParameter("referrerId").let {
                    d = it
                }
                completion(null, d)
            }
            .addOnFailureListener(activity) { e ->
                completion(e, null)
            }
    }
    
}
