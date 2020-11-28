package com.carswaddle.carswaddleandroid.services

import com.carswaddle.carswaddleandroid.services.serviceModels.*
import retrofit2.Call
import retrofit2.http.*
import java.util.*

private const val autoServiceEndpoint = "/api/auto-service"
private const val autoServiceSingleEndpoint = "/api/auto-service-details"

interface AutoServiceService {

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(autoServiceEndpoint)
    fun autoServices(@Query("limit") limit: Int, @Query("offset") offset: Int, @Query("sortStatus") sortStatus: List<String>, @Query("filterStatus") filterStatus: List<String>): Call<List<AutoService>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(autoServiceEndpoint)
    fun autoServiceDate(@Query("mechanicID") mechanicId: String, @Query("startDate") startDate: Calendar, @Query("endDate") endDate: Calendar, @Query("filterStatus") filterStatus: List<String>): Call<List<Map<String, Any>>>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @GET(autoServiceSingleEndpoint)
    fun autoServiceDetails(@Query("autoServiceID") autoServiceId: String): Call<AutoService>

    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @PATCH(autoServiceEndpoint)
    fun updateAutoService(@Query("autoServiceID") autoServiceId: String, @Body updateAutoService: UpdateAutoService): Call<AutoService>
    
    @Headers(ContentType.headerPrefix + ContentType.applicationJSON)
    @POST(autoServiceEndpoint)
    fun createAutoService(@Body uploadServiceEntity: CreateAutoService): Call<AutoService>
    
}


// offset=0&limit=100&filterStatus=scheduled&filterStatus=canceled&filterStatus=inProgress

/*
* [
    {
        "id": "69651ff0-358c-11ea-b7c8-7b3292d1eb24",
        "scheduledDate": "2020-01-13T17:00:00.000Z",
        "status": "scheduled",
        "transferID": "tr_1G0F5nDGwCXJzLurNcSsaaVw",
        "balanceTransactionID": "txn_1G0F5nIuPyeOv7fsTSuRYpjL",
        "transferAmount": 4502,
        "invoiceID": "in_1FVjhvDGwCXJzLurkjCo4gtA",
        "chargeID": "ch_1G0F5lDGwCXJzLurSjNOzoiV",
        "refundID": null,
        "transferReversalID": null,
        "notes": null,
        "createdAt": "2020-01-12T22:39:36.815Z",
        "updatedAt": "2020-01-12T22:39:36.833Z",
        "userID": "35669110-ba13-11e9-b6de-45ab229113ae",
        "mechanicID": "fbc9c0f0-b9c5-11e9-b6de-45ab229113ae",
        "couponID": null,
        "vehicleID": "5da74bc0-358c-11ea-b7c8-7b3292d1eb24",
        "user": {
            "firstName": "Sally",
            "lastName": "Swanson",
            "id": "35669110-ba13-11e9-b6de-45ab229113ae",
            "profileImageID": null,
            "email": "mechanic@carswaddle.com",
            "phoneNumber": "+18019605212",
            "timeZone": "America/Denver"
        },
        "location": {
            "id": "695c1f40-358c-11ea-b7c8-7b3292d1eb24",
            "point": {
                "type": "Point",
                "coordinates": [
                    -111.85324174204874,
                    40.36814322312216
                ]
            },
            "streetAddress": "S Bridle Path Loop 1638\nLehi UT 84043",
            "createdAt": "2020-01-12T22:39:36.757Z",
            "updatedAt": "2020-01-12T22:39:36.846Z",
            "autoServiceID": "69651ff0-358c-11ea-b7c8-7b3292d1eb24",
            "mechanicID": null
        },
        "serviceEntities": [
            {
                "id": "696bd6b0-358c-11ea-b7c8-7b3292d1eb24",
                "entityType": "OIL_CHANGE",
                "createdAt": "2020-01-12T22:39:36.860Z",
                "updatedAt": "2020-01-12T22:39:36.868Z",
                "autoServiceID": "69651ff0-358c-11ea-b7c8-7b3292d1eb24",
                "oilChangeID": "696ac540-358c-11ea-b7c8-7b3292d1eb24",
                "oilChange": {
                    "id": "696ac540-358c-11ea-b7c8-7b3292d1eb24",
                    "oilType": "CONVENTIONAL",
                    "createdAt": "2020-01-12T22:39:36.853Z",
                    "updatedAt": "2020-01-12T22:39:36.853Z"
                }
            }
        ],
        "vehicle": {
            "id": "5da74bc0-358c-11ea-b7c8-7b3292d1eb24",
            "licensePlate": "C968HK",
            "state": "UT",
            "name": "Ford Edge",
            "vin": null,
            "createdAt": "2020-01-12T22:39:17.116Z",
            "updatedAt": "2020-01-12T22:39:17.116Z",
            "userID": "35669110-ba13-11e9-b6de-45ab229113ae",
            "vehicleDescription": null
        },
        "mechanic": {
            "id": "fbc9c0f0-b9c5-11e9-b6de-45ab229113ae",
            "isActive": true,
            "stripeAccountID": "acct_1F58iKIuPyeOv7fs",
            "identityDocumentID": null,
            "identityDocumentBackID": null,
            "dateOfBirth": "1994-05-08T06:00:00.000Z",
            "profileImageID": "93d9b9a0-3cdd-11ea-9c7e-0bb6ff55c343",
            "isAllowed": true,
            "hasSetAvailability": true,
            "hasSetServiceRegion": true,
            "chargeForTravel": true,
            "createdAt": "2019-08-08T10:19:19.565Z",
            "updatedAt": "2020-01-22T06:08:16.115Z",
            "userID": "b6415a80-b415-11e9-8dc1-83c0b8a1335d",
            "user": {
                "firstName": "Rupertina",
                "lastName": "Macintosh",
                "id": "b6415a80-b415-11e9-8dc1-83c0b8a1335d",
                "profileImageID": null,
                "email": "kyle@carswaddle.com",
                "phoneNumber": "+18019605212",
                "timeZone": "America/Denver"
            }
        },
        "reviewFromUser": null,
        "reviewFromMechanic": null
    }
]
* */