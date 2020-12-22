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
