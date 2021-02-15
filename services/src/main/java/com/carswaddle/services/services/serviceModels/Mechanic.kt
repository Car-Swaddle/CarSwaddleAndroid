package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Mechanic (
    val id: String,
    val isActive: Boolean,
    val isAllowed: Boolean,
    val userID: String?,
    val user: User?,
    val scheduleTimeSpans: List<TemplateTimeSpan>?,
    val stripeAccountID: String?,
    val services: List<AutoService>?,
    val reviews: List<Review>?,
    val serviceRegion: Region?,
    val dateOfBirth: Date?,
    val address: Address?,
    val stats: Stats?,
    val profileImageID: String?,
    val pushDeviceToken: String?,
    val balance: Balance?,
    val transactions: List<Transaction>?,
    val payouts: List<Payout>,
    val identityDocumentID: String?,
    val identityDocumentBackID: String?,
    val verification: Verification?,
    val taxYears: List<TaxInfo>?,
    val bankAccount: BankAccount?,
    val hasSetAvailability: Boolean,
    val hasSetServiceRegion: Boolean,
    val creationDate: Date,
    val oilChangePricing: OilChangePricing?,
    val chargeForTravel: Boolean
)

data class UpdateMechanic(
    val isActive: Boolean? = null, // Allow new appointments
    val token: String? = null,
    val dateOfBirth: Date? = null,
    val address: UpdateMechanicAddress? = null,
    val externalAccount: String? = null, // Bank acount token. Use stripe to generate a token to represent the bank account to send up to server
    val ssnLast4: String? = null,
    val personalID: String? = null, // Social Security number
    val chargeForTravel: Boolean? = null
)


data class UpdateMechanicAddress(
    val line1: String,
    val line2: String?,
    val postalCode: String,
    val city: String,
    val state: String,
    val country: String?
)


data class UpdateAvailability(
    val spans: List<UpdateTemplateTimeSpan>
)


data class ReviewResponse(
    val reviewsGivenToMechanic: List<Review>
)
