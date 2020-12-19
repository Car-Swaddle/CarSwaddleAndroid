package com.carswaddle.carswaddleandroid.data.mechanic


class TemplateTimeSpanRepository(private val timeSpanDao: TemplateTimeSpanDao) {

//    suspend fun getMechanic(mechanicId: String): com.carswaddle.carswaddleandroid.data.mechanic.Mechanic? {
//        return mechanicDao.getMechanic(mechanicId)
//    }
//
//    suspend fun getMechanic(mechanicId: String): com.carswaddle.carswaddleandroid.data.mechanic.Mechanic? {
//        return mechanicDao.getMechanic(mechanicId)
//    }

    suspend fun getTimeSpan(id: String): TemplateTimeSpan? {
        return timeSpanDao.getTimeSpan(id)
    }

    suspend fun getTimeSpans(ids: List<String>): List<TemplateTimeSpan>? {
        return timeSpanDao.getTimeSpans(ids)
    }


}