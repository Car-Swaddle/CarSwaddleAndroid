package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanic
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.mechanic.Verification
import kotlinx.coroutines.launch

class IdentificationNumberViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepository: MechanicRepository

    private val _mechanic = MutableLiveData<Mechanic?>().apply {
        value = null
    }
    val mechanic: LiveData<Mechanic?> = _mechanic

    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepository = MechanicRepository(db.mechanicDao())
    }

    fun updateSocialSecurityLast4(last4: String, context: Context, completion: (error: Throwable?) -> Unit) {
        val mech = UpdateMechanic(ssnLast4 = last4)
        mechanicRepository.updateMechanic(mech, context) {
            completion(it)
        }
    }

    fun updateFullSocial(fullSocial: String, context: Context, completion: (error: Throwable?) -> Unit) {
        val mech = UpdateMechanic(personalID = fullSocial)
        mechanicRepository.updateMechanic(mech, context) {
            completion(it)
        }
    }

}