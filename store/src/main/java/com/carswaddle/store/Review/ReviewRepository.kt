package com.carswaddle.carswaddleandroid.data.Review

import android.content.Context
import android.util.Log
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.retrofit.ServiceNotAvailable
import com.carswaddle.carswaddleandroid.data.Review.Review as StoreReview
import com.carswaddle.carswaddleandroid.retrofit.serviceGenerator
import com.carswaddle.carswaddleandroid.services.MechanicService
import com.carswaddle.carswaddleandroid.services.ReviewResponse
import kotlinx.coroutines.*
import com.carswaddle.carswaddleandroid.services.serviceModels.Review as ServiceReview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewRepository(private val reviewDao: ReviewDao) {

    suspend fun insert(review: StoreReview) {
        reviewDao.insertReview(review)
    }

    suspend fun getReview(reviewId: String): StoreReview? {
        return reviewDao.getReview(reviewId)
    }

    suspend fun getReviews(reviewIds: List<String>): List<StoreReview> {
        return reviewDao.getReviews(reviewIds)
    }
    
    fun getReviews(context: Context, mechanicId: String?, limit: Int = 100, offset: Int = 0, completion: (error: Throwable?, reviewIds: List<String>?) -> Unit) {
        val mechanicService = ServiceGenerator.authenticated(context)?.retrofit?.create(MechanicService::class.java)
        if (mechanicService == null) {
            completion(ServiceNotAvailable(), null)
            return
        }
        val call = mechanicService.getReviews(mechanicId, limit, offset)
        call.enqueue(object : Callback<ReviewResponse> {
            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Log.d("retrofit ", "call failed")
                completion(t, null)
            }

            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                val result = response?.body()?.reviewsGivenToMechanic
                if (result == null) {
                    completion(null, null)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val reviewIds: MutableList<String> = mutableListOf()
                        for (review in result) {
                            val storeReview = StoreReview(review)
                            insert(storeReview)
                            reviewIds.add(storeReview.id)
                        }
                        completion(null, reviewIds)
                    }
                }
            }
        })
        
    }

}