package com.carswaddle.carswaddleandroid.CarSwaddleApp

import android.app.Application
import com.carswaddle.carswaddleandroid.stripe.stripePublishableKey
import com.stripe.android.PaymentConfiguration

class CarSwaddleApp: Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        PaymentConfiguration.init(
            applicationContext,
            stripePublishableKey()
        )
    }
    
}