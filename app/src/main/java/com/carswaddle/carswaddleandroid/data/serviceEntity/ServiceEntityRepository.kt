package com.carswaddle.carswaddleandroid.data.serviceEntity

import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleDao

class ServiceEntityRepository(private val serviceEntityDao: ServiceEntityDao) {

    suspend fun getServiceEntity(serviceEntityID: String): ServiceEntity? {
        return serviceEntityDao.getServiceEntity(serviceEntityID)
    }

    suspend fun getServiceEntities(autoServiceID: String): List<ServiceEntity>? {
        return serviceEntityDao.getServiceEntities(autoServiceID)
    }

}
