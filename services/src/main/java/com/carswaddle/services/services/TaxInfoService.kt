package com.carswaddle.services.services

import com.carswaddle.carswaddleandroid.services.ContentType
import com.carswaddle.carswaddleandroid.services.serviceModels.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query



private const val taxYearsEndpoint = "/api/tax-years"
private const val taxInfoEndpoint = "/api/taxes"

interface TaxInfoService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(taxYearsEndpoint)
    fun getTaxYears(): Call<List<String>?>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(taxInfoEndpoint)
    fun getTaxInfo(@Query("taxYear") taxYear: String): Call<TaxInfo>

}
