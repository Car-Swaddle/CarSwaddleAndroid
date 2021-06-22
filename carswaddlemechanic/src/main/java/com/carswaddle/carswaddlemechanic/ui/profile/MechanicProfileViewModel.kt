package com.carswaddle.carswaddlemechanic.ui.profile

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
import com.carswaddle.carswaddleandroid.services.IdDocumentImageSide
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanic
import com.carswaddle.store.mechanic.Verification as StoreVerification
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.launch


class MechanicProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepository: MechanicRepository
    private val userRepository: UserRepository

    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepository = MechanicRepository(db.mechanicDao())
        userRepository = UserRepository(db.userDao())
        
        updateCurrentMechanic(application) {}
        updateCurrentUser(application) {}
        updateStats(application) {}
        updateVerification(application) {}
    }

    /// This updates verification as well
    fun updateMechanic(
        updateMechanic: UpdateMechanic,
        context: Context,
        completion: (error: Throwable?) -> Unit
    ) {
        mechanicRepository.updateMechanic(updateMechanic, context) {
            viewModelScope.launch {
                val newMechanic = mechanicRepository.getCurrentMechanic(context)
                if (newMechanic != null) {
                    _mechanic.postValue(newMechanic)
                    updateVerification(context) {
                        completion(it)
                    }
                } else {
                    completion(it)
                }
            }
        }
    }
    
    fun updateVerification(context: Context, completion: (error: Throwable?) -> Unit) {
        mechanicRepository.updatVerification(context) {
            viewModelScope.launch {
                val verification = mechanicRepository.getCurrentMechanicVerification(context)
                if (verification != null) {
                    _verification.postValue(verification)
                }
                completion(it)
            }
        }
    }

    fun updateCurrentMechanic(context: Context, completion: (error: Throwable?) -> Unit) {
        mechanicRepository.getCurrentMechanic(context) {
            viewModelScope.launch {
                val mechanic = mechanicRepository.getCurrentMechanic(context)
                if (mechanic != null) {
                    _mechanic.postValue(mechanic)
                }
                completion(it)
            }
        }
    }

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

    fun updateStats(context: Context, completion: (error: Throwable?) -> Unit) {
        val currentMechanicId = mechanicRepository.getCurrentMechanicId(context)
        if (currentMechanicId == null) {
            completion(NoCurrentMechanicId())
            return
        }
        mechanicRepository.getStats(currentMechanicId, context) { error, mechanicId ->
            viewModelScope.launch {
                val mechanic = mechanicRepository.getCurrentMechanic(context)
                if (mechanic != null) {
                    _mechanic.postValue(mechanic)
                }
                completion(error)
            }
        }
    }
    
    fun uploadProfilePicture(filePath: String, context: Context, completion: (error: Throwable?) -> Unit) {
        mechanicRepository.uploadMechanicProfileImage(filePath, context, completion)
    }


    private val _mechanic = MutableLiveData<Mechanic?>().apply {
        value = null
    }
    val mechanic: LiveData<Mechanic?> = _mechanic

    private val _mechanicUser = MutableLiveData<User?>().apply {
        value = null
    }
    val mechanicUser: LiveData<User?> = _mechanicUser
    
    private val _verification = MutableLiveData<StoreVerification?>().apply {
        value = null
    }
    val verification: LiveData<StoreVerification?> = _verification

}

class NoCurrentMechanicId() : Throwable() {}