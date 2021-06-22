package com.carswaddle.carswaddlemechanic.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle


class CarSwaddleMechanicApp: Application() {

    override fun onCreate() {
        super.onCreate()

        CarSwaddleMechanicApp.applicationContext = applicationContext

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


    companion object {
        lateinit var applicationContext: Context
    }

}
