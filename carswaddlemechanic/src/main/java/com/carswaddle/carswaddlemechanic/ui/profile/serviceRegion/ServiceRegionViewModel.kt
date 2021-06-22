package com.carswaddle.carswaddlemechanic.ui.profile.serviceRegion

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.ErrorRequestCoordinator
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateRegion
import com.carswaddle.store.AppDatabase
import com.carswaddle.store.region.Region
import com.carswaddle.store.region.RegionRepository
import com.carswaddle.store.taxInfo.TaxInfo
import com.carswaddle.store.taxInfo.TaxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ServiceRegionViewModel(application: Application) : AndroidViewModel(application) {

    private val regionRepo: RegionRepository

    init {
        val db = AppDatabase.getDatabase(application)
        regionRepo = RegionRepository(db.regionDao())
        importRegion(application) {}
    }

    private val _region = MutableLiveData<Region?>().apply {
        value = null
    }
    val region: LiveData<Region?> = _region

    private fun importRegion(context: Context, completion: (error: Throwable?) -> Unit) {
        regionRepo.getRegion(context) { error, id ->
            if (error == null && id != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val region = regionRepo.getRegion(id)
                    _region.postValue(region)
                    completion(null)
                }
            } else {
                completion(error)
            }
        }
    }
    
    fun updateRegion(updateRegion: UpdateRegion, context: Context, completion: (error: Throwable?) -> Unit) {
        regionRepo.updateRegion(updateRegion, context) { error, id -> 
            if (error == null && id != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val region = regionRepo.getRegion(id)
                    _region.postValue(region)
                    completion(null)
                }
            } else {
                completion(error)
            }
        }
    }

}