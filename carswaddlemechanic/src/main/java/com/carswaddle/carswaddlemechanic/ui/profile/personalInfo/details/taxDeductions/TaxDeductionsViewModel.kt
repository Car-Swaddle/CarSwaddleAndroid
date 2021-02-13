package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details.taxDeductions

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.taxInfo.TaxInfo as TaxInfoStoreModel
import com.carswaddle.store.taxInfo.TaxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaxDeductionsViewModel(application: Application) : AndroidViewModel(application) {

    private val taxRepo: TaxRepository

    init {
        val db = AppDatabase.getDatabase(application)
        taxRepo = TaxRepository(db.taxDao())
        importTaxInfo(application) {}
    }
    
    private val _taxInfos = MutableLiveData<List<TaxInfoStoreModel>?>().apply {
        value = listOf()
    }
    val taxInfos: LiveData<List<TaxInfoStoreModel>?> = _taxInfos

    private fun importTaxInfo(context: Context, completion: (error: Throwable?) -> Unit) {
        taxRepo.getTaxInfos(context) {
            if (it == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val taxInfos = taxRepo.getTaxInfos()
                    _taxInfos.postValue(taxInfos)
                    completion(null)
                }
            } else {
                completion(it)
            }
        }
    }

}