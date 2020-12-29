package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.Review.Review
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocation
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntity
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle

data class AutoServiceListElements(
    val autoService: AutoService,
    val mechanic: Mechanic,
    val vehicle: Vehicle,
    val location: AutoServiceLocation,
    val mechanicUser: User,
    val serviceEntities: List<ServiceEntity>?,
    val review: Review?,
    val user: User?
)