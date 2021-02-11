package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanic
import com.carswaddle.store.AppDatabase
import java.util.*

class DateOfBirthViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepository: MechanicRepository

    private val _mechanic = MutableLiveData<Mechanic?>().apply {
        value = null
    }
    val mechanic: LiveData<Mechanic?> = _mechanic

    private val _dateOfBirth = MutableLiveData<Date?>().apply {
        value = null
    }
    val dateOfBirth: LiveData<Date?> = _dateOfBirth

    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepository = MechanicRepository(db.mechanicDao())
        getDateOfBirth(application) { }
    }
    
    fun getDateOfBirth(context: Context, completion: (error: Throwable?) -> Unit) {
        mechanicRepository.getCurrentMechanic(context) {
            val b = mechanicRepository.getCurrentMechanic(context)?.dateOfBirth?.time
            if (b != null) {
                _dateOfBirth.postValue(b)
            }
            completion(it)
        }
    }

    fun updateDateOfBirth(dateOfBirth: Date, context: Context, completion: (error: Throwable?) -> Unit) {
        val mech = UpdateMechanic(dateOfBirth = dateOfBirth)
        mechanicRepository.updateMechanic(mech, context) {
            completion(it)
        }
    }

}