package com.carswaddle.carswaddleandroid.data.location


class AutoServiceLocationRepository(private val locationDao: AutoServiceLocationDao) {

    suspend fun getLocation(locationId: String): AutoServiceLocation? {
        return locationDao.getLocation(locationId)
    }

}