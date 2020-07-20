package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.location.Location
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle

data class AutoServiceListElements(
    val autoService: AutoService,
    val mechanic: Mechanic,
    val vehicle: Vehicle,
    val location: Location,
    val mechanicUser: User
)