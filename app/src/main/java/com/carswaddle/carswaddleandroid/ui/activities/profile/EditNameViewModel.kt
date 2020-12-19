package com.carswaddle.carswaddleandroid.ui.activities.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.launch


class EditNameViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo: UserRepository


    init {
        val db = AppDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())

        loadCurrentUser()
    }


    val currentUser: LiveData<User>
        get() = _currentUser

    private val _currentUser = MutableLiveData<User>()

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val currentUser = userRepo.getCurrentUser(getApplication())
            if (currentUser != null) {
                _currentUser.value = currentUser
            } else {
                // TODO: fail better man
            }
        }
    }

    fun updatePhoneNumber(phoneNumber: String, cacheCompletion: () -> Unit, completion: (error: Error?) -> Unit) {
        userRepo.updatePhoneNumber(phoneNumber, getApplication(), cacheCompletion) { error ->
            completion(error)
        }
    }

    fun updateName(firstName: String?, lastName: String?, cacheCompletion: () -> Unit, completion: (error: Error?) -> Unit) {
        userRepo.updateName(firstName, lastName, getApplication(), cacheCompletion) { error ->
            completion(error)
        }
    }

}