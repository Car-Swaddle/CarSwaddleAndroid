package com.carswaddle.store.region

import android.content.Context
import com.carswaddle.carswaddleandroid.retrofit.EmptyResponseBody
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateRegion
import com.carswaddle.carswaddleandroid.services.serviceModels.Region as RegionServiceModel
import com.carswaddle.services.services.RegionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.carswaddle.store.region.Region as RegionStoreModel

class RegionRepository(val regionDao: RegionDao) {
    
    fun getRegion(
        context: Context,
        completion: (t: Throwable?, regionId: String?) -> Unit
    ) {
        val regionService = ServiceGenerator.authenticated(context)?.retrofit?.create(RegionService::class.java)
        if (regionService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }

        val call = regionService.getRegion()
        call.enqueue(object : Callback<RegionServiceModel> {
            override fun onFailure(call: Call<RegionServiceModel>, t: Throwable) { completion(t, null) }
            override fun onResponse(call: Call<RegionServiceModel>, response: Response<RegionServiceModel>) {
                val region = response.body()
                if (region == null) {
                    completion(EmptyResponseBody(), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertRegion(RegionStoreModel(region))
                        completion(null, region.id)
                    }
                }
            }
        })
    }

    fun updateRegion(
        updateRegion: UpdateRegion,
        context: Context,
        completion: (t: Throwable?, regionId: String?) -> Unit
    ) {
        val regionService = ServiceGenerator.authenticated(context)?.retrofit?.create(RegionService::class.java)
        if (regionService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }
        
        val call = regionService.updateRegion(updateRegion)
        call.enqueue(object : Callback<RegionServiceModel> {
            override fun onFailure(call: Call<RegionServiceModel>, t: Throwable) { completion(t, null) }
            override fun onResponse(call: Call<RegionServiceModel>, response: Response<RegionServiceModel>) {
                val region = response.body()
                if (region == null) {
                    completion(EmptyResponseBody(), null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertRegion(RegionStoreModel(region))
                        completion(null, region.id)
                    }
                }
            }
        })
    }

    suspend fun getRegion(id: String): RegionStoreModel {
        return regionDao.getRegion(id)
    }

    suspend fun insertRegion(region: RegionStoreModel) {
        regionDao.insertRegion(region)
    }

}