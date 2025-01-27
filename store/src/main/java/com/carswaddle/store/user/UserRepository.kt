package com.carswaddle.carswaddleandroid.data.user

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.retrofit.serviceGenerator
import com.carswaddle.carswaddleandroid.services.AuthenticationService
import com.carswaddle.carswaddleandroid.services.LogoutBody
import com.carswaddle.carswaddleandroid.services.UserService
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateUser
import com.carswaddle.services.Authentication
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val currentUserIdKey: String = "com.carswaddle.carswaddleandroid.user.currentUserId"
private val currentMechanicIdKey: String = "com.carswaddle.carswaddleandroid.user.currentMechanicId"

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class UserRepository(private val userDao: UserDao) {
    
    suspend fun insert(user: User) = withContext(Dispatchers.IO + NonCancellable) {
        userDao.insertUser(user)
    }

    suspend fun insert(mechanic: Mechanic) = withContext(Dispatchers.IO + NonCancellable) {
        userDao.insertMechanic(mechanic)
    }

    suspend fun getMechanic(mechanicId: String): Mechanic? {
        return userDao.getMechanic(mechanicId)
    }

    suspend fun update(user: User, updateUser: UpdateUser) = withContext(Dispatchers.IO + NonCancellable) {
        userDao.insertUser(User(user, updateUser))
    }

    fun login(email: String, password: String, isMechanic: Boolean, context: Context, completion: (error: Throwable?, response: AuthResponse?) -> Unit) {
        val auth = serviceGenerator.retrofit.create(AuthenticationService::class.java)
        val call = auth.login(email, password, isMechanic)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result?.token != null) {
                    val auth = Authentication(context)
                    val t = result.token
                    if (t != null) {
                        auth.setLoginToken(t)
                    }
                }
                val user = result?.user
                val mechanic = result?.mechanic
                if (user != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        insert(User(user))
                        setCurrentUserId(user.id, context)
                        
                        if (mechanic != null) {
                            val existingMechanic = getMechanic(mechanic.id)
                            insert(Mechanic(mechanic, existingMechanic?.averageRating, existingMechanic?.numberOfRatings, existingMechanic?.autoServicesProvided))
                            setCurrentMechanicId(mechanic.id, context)
                        }
                        
                        completion(null, result)
                        CoroutineScope(Dispatchers.Default).launch {
                            val intent = Intent(USER_DID_LOGIN)
                            intent.putExtra(IS_SIGN_UP, false)
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                        }
                    }
                } else {
                    completion(null, result)
                }
            }
        })
    }

    fun logout(deviceToken: String, context: Context, completion: (error: Throwable?, response: AuthResponse?) -> Unit) {
        val auth = serviceGenerator.retrofit.create(AuthenticationService::class.java)
        val call = auth.logout(LogoutBody(deviceToken, PUSH_TOKEN_TYPE))
        call.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }
            
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                completion(null, result)
            }
        })
    }

    fun signUp(email: String, password: String, isMechanic: Boolean, referrerId: String?, context: Context, completion: (error: Throwable?, response: AuthResponse?) -> Unit) {
        val auth = serviceGenerator.retrofit.create(AuthenticationService::class.java)
        val call = auth.signUp(email, password, isMechanic, referrerId)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result?.token != null) {
                    val auth = Authentication(context)
                    val t = result.token
                    if (t != null) {
                        auth.setLoginToken(t)
                    }
                }
                val user = result?.user
                if (user != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        insert(User(user))
                        setCurrentUserId(user.id, context)
                        completion(null, result)
                        CoroutineScope(Dispatchers.Default).launch {
                            val intent = Intent(USER_DID_LOGIN)
                            intent.putExtra(IS_SIGN_UP, true)
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                        }
                    }
                } else {
                    completion(EmailNotFoundError(""), result)
                }
            }
        })
    }

    fun sendSMSVerificationSMS(context: Context, completion: (error: Throwable?) -> Unit) {
        val auth = ServiceGenerator.authenticated(context)?.retrofit?.create(AuthenticationService::class.java)
        val call = auth?.sendSMSVerification()
        call?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t)
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                Log.d("retrofit ", "call succeeded")
                
                val r = response
                if (r == null) {
                    completion(ServiceError())
                    return
                }
                if (r.code() >= 400 && r.code() < 500) {
                    completion(ServiceError())
                } else {
                    completion(null)
                }
                
            }
        })
    }
    
    fun sendEmailVerification(context: Context, completion: (throwable: Throwable?) -> Unit) {
        val auth = ServiceGenerator.authenticated(context)?.retrofit?.create(UserService::class.java)
        if (auth == null) {
            completion(ServiceNotAvailable())
            return
        }
        val call = auth?.sendEmailVerification()
        call?.enqueue(object : Callback<Map<String, Any>> {
            override fun onFailure(call: Call<Map<String, Any>>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t)
            }

            override fun onResponse(call: Call<Map<String, Any>>?, response: Response<Map<String, Any>>?) {
                Log.d("retrofit ", "call succeeded")
                val code = response?.code()
                if (code == null) {
                    completion(null)
                } else if (code == 404) {
                    val e = EmailNotFoundError("No email was found")
                    completion(e)
                } else if (code < 200 || code >= 300) {
                    val e = EmailNotFoundError("Unable to send email")
                    completion(e)
                } else {
                    // Code is between 200 and 299 inclusive
                    completion(null)
                }
            }
        })
    }

    fun sendResetLink(email: String, context: Context, completion: (error: Throwable?) -> Unit) {
        val auth = serviceGenerator.retrofit?.create(AuthenticationService::class.java)
        val call = auth?.requestResetPasswordLink(email, "car-swaddle-mechanic")
        call?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t)
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                Log.d("retrofit ", "call succeeded")
                val code = response?.code()
                if (code == null) {
                    completion(null)
                } else if (code == 404) {
                    val e = EmailNotFoundError("No email was found")
                    completion(e)
                } else if (code < 200 || code >= 300) {
                    val e = EmailNotFoundError("Unable to send email")
                    completion(e)
                } else {
                    // Code is between 200 and 299 inclusive
                    completion(null)
                }
            }
        })
    }

    fun resetPassword(newPassword: String, resetToken: String, completion: (error: Throwable?) -> Unit) {
        val auth = serviceGenerator.retrofit?.create(AuthenticationService::class.java)
        val call = auth?.resetPassword(newPassword, resetToken)
        call?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("retrofit", "call failed")
                completion(t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("retrofit ", "call succeeded")
                val code = response?.code()
                if (code < 200 || code >= 300) {
                    val e = Throwable("Unable to reset password")
                    completion(e)
                } else {
                    completion(null)
                }
            }
        })
    }

    fun verifySMSCode(context: Context, code: String, completion: (error: Throwable?) -> Unit) {
        val auth = ServiceGenerator.authenticated(context)?.retrofit?.create(AuthenticationService::class.java)
        val call = auth?.verifySMS(code)
        call?.enqueue(object : Callback<com.carswaddle.carswaddleandroid.services.serviceModels.User> {
            override fun onFailure(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(t)
            }

            override fun onResponse(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>?, response: Response<com.carswaddle.carswaddleandroid.services.serviceModels.User>?) {
                Log.d("retrofit ", "call succeeded")
                val user = response?.body()
                if (user != null && user.isPhoneNumberVerified == true) {
                    Log.d("retrofit ", "call succeeded")
                    CoroutineScope(Dispatchers.IO).async {
                        val dataUser = User(user)
                        insert(dataUser)
                        completion(null)
                    }
                } else {
                    val e = Throwable("unable to verify")
                    completion(e)
                }
            }
        })
    }

    fun importCurrentUser(context: Context, completion: (error: Error?) -> Unit) {
        val userService = ServiceGenerator.authenticated(context)?.retrofit?.create(UserService::class.java)
        if (userService == null) {
            // TODO: call with error
            completion(null)
            return
        }

        val call = userService.currentUser()
        call.enqueue(object : Callback<com.carswaddle.carswaddleandroid.services.serviceModels.User> {
            override fun onFailure(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(Error(t.localizedMessage))
            }

            override fun onResponse(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>, response: Response<com.carswaddle.carswaddleandroid.services.serviceModels.User>) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result == null) {
                    // TODO: make an error here
                    completion(null) // somethind
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = User(result)
                        insert(user)
                        setCurrentUserId(user.id, context)
                        completion(null)
                    }
                }
            }
        })
    }

    fun updateReferrerId(referrerId: String?, context: Context, cacheCompletion: () -> Unit = {}, completion: (throwable: Throwable?) -> Unit) {
        update(null, null, null, null, referrerId, null, null, cacheCompletion, context, completion)
    }

    fun updateName(firstName: String?, lastName: String?, context: Context, cacheCompletion: () -> Unit = {}, completion: (throwable: Throwable?) -> Unit) {
        update(firstName, lastName, null, null, null, null, null, cacheCompletion, context, completion)
    }

    fun updatePhoneNumber(phoneNumber: String, context: Context, cacheCompletion: () -> Unit = {}, completion: (throwable: Throwable?) -> Unit) {
        update(null, null, phoneNumber, null, null, null, null, cacheCompletion, context, completion)
    }

    fun updatePushToken(token: String, context: Context, cacheCompletion: () -> Unit = {}, completion: (throwable: Throwable?) -> Unit) {
        update(null, null, null, token, null, null, null, cacheCompletion, context, completion)
    }

    private fun update(updateUser: UpdateUser, context: Context, cacheCompletion: () -> Unit = {}, completion: (throwable: Throwable?) -> Unit) {
        val userService = ServiceGenerator.authenticated(context)?.retrofit?.create(UserService::class.java)
        if (userService == null) {
            // TODO: call with error
            completion(ServiceNotAvailable())
            return
        }

        val user = getCurrentUser(context)
        if (user != null) {
            CoroutineScope(Dispatchers.IO).async {
                update(user, updateUser)
                cacheCompletion()
            }
        }

        val call = userService.updateUser(updateUser)
        call.enqueue(object : Callback<com.carswaddle.carswaddleandroid.services.serviceModels.User> {
            override fun onFailure(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(Error(t.localizedMessage))
            }

            override fun onResponse(call: Call<com.carswaddle.carswaddleandroid.services.serviceModels.User>, response: Response<com.carswaddle.carswaddleandroid.services.serviceModels.User>) {
                Log.d("retrofit ", "call succeeded")
                val result = response?.body()
                if (result == null) {
                    // TODO: make an error here
                    Log.d("retrofit ", "call failed")
                    completion(ServiceError())
                } else {
                    Log.d("retrofit ", "call succeeded")
                    CoroutineScope(Dispatchers.IO).async {
                        val user = User(result)
                        insert(user)
                        completion(null)
                    }
                }
            }
        })
    }

    private fun update(firstName: String?, lastName: String?, phoneNumber: String?, token: String?, referrerId: String?, timeZone: String?, adminKey: String?, cacheCompletion: () -> Unit, context: Context, completion: (throwable: Throwable?) -> Unit) {
        val updateUser = UpdateUser(firstName, lastName, phoneNumber, token, referrerId, PUSH_TOKEN_TYPE, timeZone, adminKey)
        update(updateUser, context, cacheCompletion, completion)
    }

    fun getCurrentUser(context: Context): User? {
        val userId = getCurrentUserId(context) ?: return null
        return userDao.getUserWithUserId(userId)
    }

    fun getUser(userId: String): User? {
        return userDao.getUserWithUserId(userId)
    }

    fun getCurrentUserId(context: Context): String? {
        val preferences = context.carSwaddlePreferences()
        return preferences.getString(currentUserIdKey, null)
    }

    fun setCurrentUserId(userId: String, context: Context) {
        val editContext = context.carSwaddlePreferences().edit()
        editContext.putString(currentUserIdKey, userId)
        editContext.apply()
    }

    fun setCurrentMechanicId(mechanicId: String, context: Context) {
        val editContext = context.carSwaddlePreferences().edit()
        editContext.putString(currentMechanicIdKey, mechanicId)
        editContext.apply()
    }

    fun getCurrentMechanicId(context: Context): String? {
        val preferences = context.carSwaddlePreferences()
        return preferences.getString(currentMechanicIdKey, null)
    }
    
    companion object {
        
        const val USER_DID_LOGIN = "UserRepository.USER_DID_LOGIN"
        const val IS_SIGN_UP = "UserRepository.IS_SIGN_UP"
        
        const val PUSH_TOKEN_TYPE = "FCM"
        
    }

}



class EmailNotFoundError(message: String) : Throwable(message) {}

class ServiceError() : Throwable() {}

class MechanicIdIsUnavailable() : Throwable() {}
