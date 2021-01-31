package com.carswaddle.carswaddleandroid.data.mechanic


class TemplateTimeSpanRepository(private val timeSpanDao: TemplateTimeSpanDao) {

    suspend fun getTimeSpan(id: String): TemplateTimeSpan? {
        return timeSpanDao.getTimeSpan(id)
    }

    suspend fun getTimeSpans(ids: List<String>): List<TemplateTimeSpan>? {
        return timeSpanDao.getTimeSpans(ids)
    }
    
}