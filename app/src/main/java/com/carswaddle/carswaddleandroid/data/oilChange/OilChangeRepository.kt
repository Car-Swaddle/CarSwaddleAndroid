package com.carswaddle.carswaddleandroid.data.oilChange

import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntity
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityDao

class OilChangeRepository(private val oilChangeDao: OilChangeDao) {

    suspend fun getOilChange(oilChangeID: String): OilChange? {
        return oilChangeDao.getOilChange(oilChangeID)
    }

}