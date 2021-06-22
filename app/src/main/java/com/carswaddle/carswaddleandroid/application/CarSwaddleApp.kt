package com.carswaddle.carswaddleandroid.CarSwaddleApp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.carswaddle.carswaddleandroid.messaging.Intercom
import com.carswaddle.carswaddleandroid.retrofit.*
import com.carswaddle.carswaddleandroid.stripe.stripePublishableKey
import com.stripe.android.PaymentConfiguration
import io.branch.referral.Branch


class CarSwaddleApp: Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        setupBranchIo()
        
        CarSwaddleApp.applicationContext = applicationContext
        Intercom.shared = Intercom(this)
        
        PaymentConfiguration.init(
            applicationContext,
            stripePublishableKey()
        )
        
        registerActivityLifecycleCallbacks(object: Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
    
    private fun setupBranchIo() {
        val isBranchInTestMode = when(server) {
            Server.staging -> true
            Server.production -> false
            Server.local -> true
        }
        if (isBranchInTestMode) {
            Branch.enableTestMode()
        } else {
            Branch.disableTestMode()
        }

        Branch.enableLogging()
        Branch.getAutoInstance(this)
    }
    
    companion object {
        lateinit var applicationContext: Context
    }
    
}
