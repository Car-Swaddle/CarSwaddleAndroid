package com.carswaddle.carswaddlemechanic.ui.profile.pricing

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpanRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateOilChangePricing
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateTemplateTimeSpan
import com.carswaddle.carswaddlemechanic.ui.profile.NoCurrentMechanicId
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.mechanic.OilChangePricing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PricingViewModel(application: Application) : AndroidViewModel(application) {
    
    private val mechanicRepo: MechanicRepository

    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepo = MechanicRepository(db.mechanicDao())

        importOilChangePricing(application) { e, o -> }
    }

    private val _oilchangePricing = MutableLiveData<OilChangePricing?>().apply {
        value = null
    }
    val oilChangePricing: LiveData<OilChangePricing?> = _oilchangePricing

    private fun importOilChangePricing(context: Context, completion: (error: Throwable?, oilChangePricing: OilChangePricing?) -> Unit) {
        mechanicRepo.getOilChangePricing(null, context) { error, oilChangePricing ->
            val o = oilChangePricing
            if (o == null) {
                completion(error, null)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    _oilchangePricing.postValue(o)
                }
            }
        }
    }

    fun updateOilChangePricing(updateOilChangePricing: UpdateOilChangePricing, context: Context, completion: (error: Throwable?, oilChangePricing: OilChangePricing?) -> Unit) {
        mechanicRepo.updateOilChangePricing(updateOilChangePricing, context) { error, oilChangePricing ->
            val o = oilChangePricing
            if (o == null) {
                completion(error, null)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    _oilchangePricing.postValue(o)
                    completion(null, o)
                }
            }
        }
    }

}