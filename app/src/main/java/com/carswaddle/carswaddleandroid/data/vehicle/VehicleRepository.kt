package com.carswaddle.carswaddleandroid.data.vehicle

import android.util.Log
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.retrofit.serviceGenerator
import com.carswaddle.carswaddleandroid.services.AuthenticationService
import com.carswaddle.carswaddleandroid.services.VehicleService
import com.carswaddle.carswaddleandroid.services.serviceModels.AuthResponse
import com.carswaddle.carswaddleandroid.services.serviceModels.Vehicle as VehicleModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VehicleRepository(private val vehicleDao: VehicleDao) {

    suspend fun getVehicle(vehicleId: String): Vehicle? {
        return vehicleDao.getVehicle(vehicleId)
    }

    suspend fun getVehicles(vehicleIds: List<String>): List<Vehicle> {
        return vehicleDao.getVehicles(vehicleIds)
    }
    
    suspend fun insert(vehicle: Vehicle) {
        vehicleDao.insertVehicle(vehicle)
    }
    
    fun getVehicles(limit: Int, offset: Int, completion: (error: Error?, vehicleIds: List<String>?) -> Unit) {
        val vehicleService = serviceGenerator.retrofit.create(VehicleService::class.java)
        val call = vehicleService.getVehicles(limit, offset)
        call.enqueue(object : Callback<List<VehicleModel>> {
            override fun onFailure(call: Call<List<VehicleModel>>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(null, null)
            }

            override fun onResponse(call: Call<List<VehicleModel>>?, response: Response<List<VehicleModel>>?) {
                val vehicles = response?.body()
                
                if (vehicles == null) {
                    completion(null, null)
                    return 
                }
                
                CoroutineScope(Dispatchers.IO).launch {
                    var vehicleIds: MutableList<String> = mutableListOf()
                    for (v in vehicles) {
                        val vehicle = Vehicle(v)
                        insert(vehicle)
                        vehicleIds.add(vehicle.id)
                    }
                    completion(null, vehicleIds.toList())
                }
            }

        })
    }

    fun createVehicle(name: String, field: String, vin: String, state: String, completion: (error: Error?, vehicleId: String?) -> Unit) {
        val vehicleService = serviceGenerator.retrofit.create(VehicleService::class.java)
        val call = vehicleService.createVehicle(name, field, vin, state)
        
        call.enqueue(object : Callback<VehicleModel> {
            override fun onFailure(call: Call<VehicleModel>?, t: Throwable?) {
                Log.d("retrofit ", "call failed")
                completion(null, null)
            }

            override fun onResponse(call: Call<VehicleModel>?, response: Response<VehicleModel>?) {
                val vehicle = response?.body()

                if (vehicle == null) {
                    completion(null, null)
                    return
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val v = Vehicle(vehicle)
                    completion(null, v.id)
                }
            }

        })
    }

}