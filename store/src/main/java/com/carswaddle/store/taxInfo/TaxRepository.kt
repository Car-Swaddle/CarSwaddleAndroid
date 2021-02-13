package com.carswaddle.store.taxInfo

import android.content.Context
import com.carswaddle.carswaddleandroid.generic.DispatchGroup
import com.carswaddle.carswaddleandroid.retrofit.EmptyResponseBody
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.services.services.TaxInfoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.carswaddle.carswaddleandroid.services.serviceModels.TaxInfo as TaxInfoServiceModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaxRepository(val taxDao: TaxDao) {
    
    fun getTaxInfoYears(
        context: Context,
        completion: (t: Throwable?, years: List<String>?) -> Unit
    ) {
        val taxService = ServiceGenerator.authenticated(context)?.retrofit?.create(TaxInfoService::class.java)
        if (taxService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }

        val call = taxService.getTaxYears()
        call.enqueue(object :
            Callback<List<String>?> {

            override fun onFailure(call: Call<List<String>?>, t: Throwable) {
                completion(t, null)
            }

            override fun onResponse(
                call: Call<List<String>?>,
                response: Response<List<String>?>
            ) {
                val payoutResponse = response.body()
                if (payoutResponse == null) {
                    completion(EmptyResponseBody(), null)
                } else {
                    completion(null, payoutResponse)
                }
            }
        })
    }

    fun getTaxYear(year: String, context: Context, completion: (t: Throwable?) -> Unit) {
        val taxService = ServiceGenerator.authenticated(context)?.retrofit?.create(TaxInfoService::class.java)
        if (taxService == null) { completion(ServiceNotAvailable()); return }

        val call = taxService.getTaxInfo(year)
        call.enqueue(object : Callback<TaxInfoServiceModel> {
            override fun onFailure(call: Call<TaxInfoServiceModel>, t: Throwable) { completion(t) }
            override fun onResponse(call: Call<TaxInfoServiceModel>, response: Response<TaxInfoServiceModel>) {
                val taxInfoResponse = response.body()
                if (taxInfoResponse == null) {
                    completion(EmptyResponseBody())
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertTaxInfo(taxInfoResponse)
                        completion(null)
                    }
                }
            }
        })
    }
    
    fun getTaxInfos(context: Context, completion: (t: Throwable?) -> Unit) {
        getTaxInfoYears(context) { error, years ->
            
            if (error != null || years == null) {
                completion(error ?: EmptyResponseBody())
                return@getTaxInfoYears
            }
            
            val group = DispatchGroup()
            
            var errorsFetchingYear = false
            
            for (year in years) {
                group.enter()
                getTaxYear(year, context) {
                    if (it != null) { errorsFetchingYear = true }
                    group.leave()
                } 
            }
            
            group.notify {
                val finalError: Throwable? = if (errorsFetchingYear) EmptyResponseBody() else null
                completion(finalError)
            }
            
        }
    }
    

    suspend fun getTaxInfos(): List<TaxInfo> {
        return taxDao.getTaxInfos()
    }
    
    suspend fun insertTaxInfos(taxInfos: List<TaxInfoServiceModel>) {
        var storeTaxInfos: MutableList<TaxInfo> = mutableListOf()
        for (info in taxInfos) {
            storeTaxInfos.add(TaxInfo(info))
        }
        taxDao.insertTaxInfos(storeTaxInfos)
    }

    fun insertTaxInfo(taxInfo: TaxInfoServiceModel) {
        return taxDao.insertTaxInfo(TaxInfo(taxInfo))
    }

}