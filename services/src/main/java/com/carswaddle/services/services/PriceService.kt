package com.carswaddle.carswaddleandroid.services

import com.carswaddle.services.services.serviceModels.CodeCheck
import com.carswaddle.services.services.serviceModels.PriceResponse
import retrofit2.Call
import retrofit2.http.*

private const val priceEndpoint = "/api/price"

interface PriceService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(priceEndpoint)
    fun getPrice(@Body priceRequest: PriceRequest): Call<PriceResponse>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET("$priceEndpoint/codes/{code}")
    fun getCodeCheck(@Path("code") code: String): Call<CodeCheck>
    
}

data class PriceRequest(
    val location: LocationJSON,
    val mechanicID: String,
    val oilType: String,
    val coupon: String?,
    val giftCardCodes: Collection<String>?
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
