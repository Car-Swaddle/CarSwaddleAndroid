package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TemplateTimeSpanDao {

    @Query("SELECT * FROM TemplateTimeSpan WHERE id is (:id)")
    fun getTimeSpan(id: String): TemplateTimeSpan?

    @Query("SELECT * FROM TemplateTimeSpan WHERE id in (:ids)")
    fun getTimeSpans(ids: List<String>): List<TemplateTimeSpan>?

}
