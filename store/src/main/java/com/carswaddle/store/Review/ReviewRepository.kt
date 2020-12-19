package com.carswaddle.carswaddleandroid.data.Review

import android.content.Context
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceDao
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.services.AutoServiceService
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateAutoService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewRepository(private val reviewDao: ReviewDao) {

    suspend fun insert(review: Review) {
        reviewDao.insertReview(review)
    }

    suspend fun getReview(reviewId: String): Review? {
        return reviewDao.getReview(reviewId)
    }

}