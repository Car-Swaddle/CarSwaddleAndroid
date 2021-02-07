package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateOilChangePricing
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.mechanic.OilChangePricing
import com.carswaddle.store.mechanic.Verification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PersonalInformationViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepository: MechanicRepository

    private val _mechanic = MutableLiveData<Mechanic?>().apply {
        value = null
    }
    val mechanic: LiveData<Mechanic?> = _mechanic

    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepository = MechanicRepository(db.mechanicDao())
        updateVerification(application) {}
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

    private val _verification = MutableLiveData<Verification?>().apply {
        value = null
    }
    val verification: LiveData<Verification?> = _verification

}