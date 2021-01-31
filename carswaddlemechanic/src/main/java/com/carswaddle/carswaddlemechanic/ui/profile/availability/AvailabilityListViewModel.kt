package com.carswaddle.carswaddlemechanic.ui.profile.availability

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.Review.Review
import com.carswaddle.carswaddleandroid.data.Review.ReviewRepository
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddlemechanic.ui.profile.NoCurrentMechanicId
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvailabilityListViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    private val reviewRepo: ReviewRepository
    private val mechanicRepo: MechanicRepository

    init {
        val db = AppDatabase.getDatabase(application)
        userRepository = UserRepository(db.userDao())
        reviewRepo = ReviewRepository(db.reviewDao())
        mechanicRepo = MechanicRepository(db.mechanicDao())
        
        updateTimeSlots(application) { t, ids -> }
    }

    private val _reviews = MutableLiveData<List<Review>>().apply {
        value = listOf()
    }
    val reviews: LiveData<List<Review>> = _reviews
    
    fun updateTimeSlots(context: Context, completion: (error: Throwable?, reviewIds: List<String>?) -> Unit) {
        val currentMechanicId = mechanicRepo.getCurrentMechanicId(context)
        if (currentMechanicId == null) {
            completion(NoCurrentMechanicId(), null)
            return
        }
    
        mechanicRepo.getTimeSlots(currentMechanicId, context) { error, ids ->
            val reviewIds = ids
            if (reviewIds == null) { 
                completion(error, null)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val reviews = reviewRepo.getReviews(reviewIds)
                    _reviews.postValue(reviews)
                }
            }
        }
    }
    
}