package com.carswaddle.carswaddlemechanic.ui.profile.contact

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.launch

class ContactInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        val db = AppDatabase.getDatabase(application)
        userRepository = UserRepository(db.userDao())
        
        updateCurrentUser(application) {}
    }

    private val _mechanicUser = MutableLiveData<User?>().apply {
        value = null
    }
    val mechanicUser: LiveData<User?> = _mechanicUser


    fun updateCurrentUser(context: Context, completion: (error: Throwable?) -> Unit) {
        userRepository.importCurrentUser(context) {
            viewModelScope.launch {
                val user = userRepository.getCurrentUser(context)
                if (user != null) {
                    _mechanicUser.postValue(user)
                }
                completion(it)
            }
        }
    }
    
    fun resendVerificationEmail(completion: (error: Throwable?) -> Unit) {
        userRepository.sendEmailVerification(completion)
    }
    
}