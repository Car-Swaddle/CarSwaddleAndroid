package com.carswaddle.carswaddleandroid.pushNotifications
import android.content.*
import android.content.ContentValues.TAG
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.carswaddle.carswaddleandroid.CarSwaddleApp.CarSwaddleApp
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


private val DEVICE_TOKEN_KEY = "DEVICE_TOKEN_KEY"


class MessagingController: FirebaseMessagingService() {
    
    var userRepo: UserRepository
    
    
    init {
        LocalBroadcastManager.getInstance(CarSwaddleApp.applicationContext).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                registerPushToken()
            }
        }, IntentFilter(UserRepository.USER_DID_LOGIN))

        LocalBroadcastManager.getInstance(CarSwaddleApp.applicationContext).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                deletePushToken()
            }
        }, IntentFilter(Authentication.USER_WILL_LOGOUT))
        
        val db = AppDatabase.getDatabase(CarSwaddleApp.applicationContext)
        userRepo = UserRepository(db.userDao())
    }
    
    fun registerPushToken() {
        getPushNotificationToken { 
            if (it != null) {
                registerNewPushToken(it)
            }
        }
    }
    
    private fun getPushNotificationToken(completion: (token: String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                completion(null)
                return@OnCompleteListener
            }
            completion(task.result)
        })
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        registerNewPushToken(token)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        
        Log.w(TAG, "Got a message")
    }
    
    private fun registerNewPushToken(token: String) {
        userRepo.updatePushToken(token, CarSwaddleApp.applicationContext, {
            // cache completion
        }) {
            // server completion
            if (it == null) {
                saveDeviceTokenLocally(token)
            } else {
                Log.w(TAG, "unable to register device token")
            }
        }
    }
    
    private fun deletePushToken() {
        val t = lastSavedOnServerDeviceToken()
        if (t != null) {
            userRepo.logout(t, CarSwaddleApp.applicationContext) { error, response ->
                if (error == null) {
                    deleteDeviceTokenLocally()
                } else {
                    Log.w(TAG, "unable to delete device token")
                }
            }
        }
    }

    fun lastSavedOnServerDeviceToken(): String? {
        return preferences().getString(DEVICE_TOKEN_KEY, null)
    }
    
    fun saveDeviceTokenLocally(token: String) {
        val editContext = preferences().edit()
        editContext.putString(DEVICE_TOKEN_KEY, token)
        editContext.apply()
    }

    fun deleteDeviceTokenLocally() {
        val editContext = preferences().edit()
        editContext.putString(DEVICE_TOKEN_KEY, null)
        editContext.apply()
    }

    private fun preferences(): SharedPreferences {
        return CarSwaddleApp.applicationContext.carSwaddlePreferences()
    }
    
    companion object {
        
        var instance: MessagingController = MessagingController()
        
        fun initialize() {
            val i = instance
        }
        
    }
    
}
 