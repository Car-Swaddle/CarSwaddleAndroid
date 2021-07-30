package com.carswaddle.services.services.serviceModels

import com.carswaddle.carswaddleandroid.services.CouponErrorType
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.Coupon
import com.carswaddle.carswaddleandroid.services.serviceModels.User
import java.util.*

data class CodeCheckResponse (
    val error: CouponErrorType,
    val coupon: Coupon,
    val giftCard: GiftCard,
    val redeemMessage: String
)