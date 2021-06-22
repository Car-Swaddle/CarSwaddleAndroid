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
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpanRepository
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateTemplateTimeSpan
import com.carswaddle.carswaddlemechanic.ui.profile.NoCurrentMechanicId
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvailabilityListViewModel(application: Application) : AndroidViewModel(application) {

//    private val userRepository: UserRepository
//    private val reviewRepo: ReviewRepository
    private val mechanicRepo: MechanicRepository
    private val templateTimeSpanRepo: TemplateTimeSpanRepository

    init {
        val db = AppDatabase.getDatabase(application)
        templateTimeSpanRepo = TemplateTimeSpanRepository(db.templateTimeSpanDao())
        mechanicRepo = MechanicRepository(db.mechanicDao())
        
        importTimeSlots(application) { t, ids -> }
    }

//    private val _reviews = MutableLiveData<List<Review>>().apply {
//        value = listOf()
//    }
//    val reviews: LiveData<List<Review>> = _reviews

    private val _selectedTimeSlots = MutableLiveData<List<TemplateTimeSpan>>().apply {
        value = listOf()
    }
    val selectedTimeSlots: LiveData<List<TemplateTimeSpan>> = _selectedTimeSlots
    
    private fun importTimeSlots(context: Context, completion: (error: Throwable?, timeSpanIds: List<String>?) -> Unit) {
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
                    val spans = templateTimeSpanRepo.getTimeSpans(ids)
                    _selectedTimeSlots.postValue(spans)
                }
            }
        }
    }
    
    fun updateAvailability(updateTimeSpans: List<UpdateTemplateTimeSpan>, context: Context, completion: (error: Throwable?, timeSpanIds: List<String>?) -> Unit) {
        mechanicRepo.updateAvailability(updateTimeSpans, context) { error, ids ->
            completion(error, ids)
        }
    }
    
}