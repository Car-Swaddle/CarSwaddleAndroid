package com.carswaddle.carswaddlemechanic.pushNotifications
import android.content.*
import android.content.ContentValues.TAG
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddlemechanic.application.CarSwaddleMechanicApp
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository


private val DEVICE_TOKEN_KEY = "DEVICE_TOKEN_KEY"


class MessagingController: FirebaseMessagingService() {
    
    var mechanicRepo: MechanicRepository
    var userRepo: UserRepository
    
    
    init {
        LocalBroadcastManager.getInstance(CarSwaddleMechanicApp.applicationContext).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                registerPushToken()
            }
        }, IntentFilter(UserRepository.USER_DID_LOGIN))

        LocalBroadcastManager.getInstance(CarSwaddleMechanicApp.applicationContext).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                deletePushToken()
            }
        }, IntentFilter(Authentication.USER_WILL_LOGOUT))
        
        val db = AppDatabase.getDatabase(CarSwaddleMechanicApp.applicationContext)
        userRepo = UserRepository(db.userDao())
        mechanicRepo = MechanicRepository(db.mechanicDao())
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
        mechanicRepo.updatePushToken(token, CarSwaddleMechanicApp.applicationContext, {
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
            userRepo.logout(t, CarSwaddleMechanicApp.applicationContext) { error, response ->
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
    
    private fun saveDeviceTokenLocally(token: String) {
        val editContext = preferences().edit()
        editContext.putString(DEVICE_TOKEN_KEY, token)
        editContext.apply()
    }

    private fun deleteDeviceTokenLocally() {
        val editContext = preferences().edit()
        editContext.putString(DEVICE_TOKEN_KEY, null)
        editContext.apply()
    }

    private fun preferences(): SharedPreferences {
        return CarSwaddleMechanicApp.applicationContext.carSwaddlePreferences()
    }
    
    companion object {
        
        var instance: MessagingController = MessagingController()
        
        fun initialize() {
            val i = instance
        }
        
    }
    
}
 