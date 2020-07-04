package com.carswaddle.carswaddleandroid.data.location


class LocationRepository(private val locationDao: LocationDao) {

    suspend fun getLocation(locationId: String): Location? {
        return locationDao.getLocation(locationId)
    }

}