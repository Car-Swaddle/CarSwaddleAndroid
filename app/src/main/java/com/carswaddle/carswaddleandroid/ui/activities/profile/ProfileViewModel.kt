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

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo: UserRepository

    val currentUser: LiveData<User>
        get() = _currentUser
    
    private val _currentUser = MutableLiveData<User>()

    init {
        val db = AppDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())
        
        loadCurrentUser()
    }

    private fun loadCurrentUser() {

        viewModelScope.launch {
            _currentUser.postValue(userRepo.getCurrentUser(getApplication()))
        }

        userRepo.importCurrentUser(getApplication()) { error ->
            viewModelScope.launch {
                val currentUser = userRepo.getCurrentUser(getApplication())
                if (currentUser != null) {
                    _currentUser.postValue(currentUser)
                } else {
                    // TODO: fail better man
                }
            }
        }

    }

}