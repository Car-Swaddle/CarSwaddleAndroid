package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.Price
import com.carswaddle.carswaddleandroid.services.serviceModels.PriceResponse
import retrofit2.Call
import retrofit2.http.*
import java.lang.Error

private const val priceEndpoint = "/api/price"

interface PriceService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(priceEndpoint)
    fun getPrice(@Body priceRequest: PriceRequest): Call<PriceResponse>
    
}

data class PriceRequest(
    val location: LocationJSON,
    val mechanicID: String,
    val oilType: String,
    val coupon: String?
)

data class LocationJSON (
    val latitude: Double,
    val longitude: Double
)

enum class CouponErrorType {
    INCORRECT_CODE,
    EXPIRED,
    INCORRECT_MECHANIC,
    DEPLETED_REDEMPTIONS,
    OTHER
}

class CouponError(val couponErrorType: CouponErrorType): Throwable()
