package com.carswaddle.carswaddleandroid.data.user

import android.content.Context
import android.util.Log
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.services.UserService
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateUser
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val currentUserIdKey: String = "com.carswaddle.carswaddleandroid.user.currentUserId"


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class UserRepository(private val userDao: UserDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
//    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
//
//    suspend fun insert(word: Word) {
//        wordDao.insert(word)
//    }

    suspend fun insert(user: User) = withContext(Dispatchers.IO + NonCancellable) {
        userDao.insertUser(user)
    }

    suspend fun update(user: User, updateUser: UpdateUser) = withContext(Dispatchers.IO + NonCancellable) {
        userDao.insertUser(User(user, updateUser))
    }

    fun updateCurrentUser(context: Context, completion: (error: Error?) -> Unit) {
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
                    GlobalScope.async {
                        val user = User(result)
                        insert(user)
                        setCurrentUserId(user.id, context)
                        completion(null)
                    }
                }
            }
        })
    }

    fun updateName(firstName: String?, lastName: String?, context: () -> Unit, cacheCompletion: () -> Unit = {}, completion: (error: Error?) -> Unit) {
        update(firstName, lastName, null, null, null, null, cacheCompletion, context, completion)
    }

    fun updatePhoneNumber(phoneNumber: String, context: Context, cacheCompletion: () -> Unit = {}, completion: (error: Error?) -> Unit) {
        update(null, null, phoneNumber, null, null, null, cacheCompletion, context, completion)
    }

    private fun update(updateUser: UpdateUser, context: Context, cacheCompletion: () -> Unit = {}, completion: (error: Error?) -> Unit) {
        val userService = ServiceGenerator.authenticated(context)?.retrofit?.create(UserService::class.java)
        if (userService == null) {
            // TODO: call with error
            completion(null)
            return
        }

        val user = getCurrentUser(context)
        if (user != null) {
            GlobalScope.async {
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
                    completion(null) // somethind
                } else {
                    Log.d("retrofit ", "call succeeded")
                    GlobalScope.async {
                        val user = User(result)
                        insert(user)
                        completion(null)
                    }
                }
            }
        })
    }

    private fun update(firstName: String?, lastName: String?, phoneNumber: String?, token: String?, timeZone: String?, adminKey: String?, cacheCompletion: () -> Unit, context: Context, completion: (error: Error?) -> Unit) {
        val updateUser = UpdateUser(firstName, lastName, phoneNumber, token, timeZone, adminKey)
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

}