package com.carswaddle.carswaddleandroid.data.mechanic

class MechanicRepository(private val mechanicDao: MechanicDao) {

    suspend fun getMechanic(mechanicId: String): Mechanic? {
        return mechanicDao.getMechanic(mechanicId)
    }

}