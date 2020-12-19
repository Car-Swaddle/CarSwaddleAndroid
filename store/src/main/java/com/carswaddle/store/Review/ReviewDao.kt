package com.carswaddle.carswaddleandroid.data.Review

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
public abstract class ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReview(review: Review)

    @Query("SELECT * FROM review WHERE id is (:reviewId)")
    abstract fun getReview(reviewId: String): Review?
    
}