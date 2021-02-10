package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanicAddress
import com.carswaddle.store.AppDatabase

class BankAccountViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepository: MechanicRepository

    private val _mechanic = MutableLiveData<Mechanic?>().apply {
        value = null
    }
    val mechanic: LiveData<Mechanic?> = _mechanic

    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepository = MechanicRepository(db.mechanicDao())
    }

    fun updateBankAccountToken(bankAccountToken: String, context: Context, completion: (error: Throwable?) -> Unit) {
        val mech = UpdateMechanic(externalAccount = bankAccountToken)
        mechanicRepository.updateMechanic(mech, context) {
            completion(it)
        }
    }

}