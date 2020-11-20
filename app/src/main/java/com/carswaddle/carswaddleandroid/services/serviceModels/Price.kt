package com.carswaddle.carswaddleandroid.services.serviceModels

data class Price (
    var identifier: String,
    val autoServiceId: String, 
    var oilChangeCost: Int,
    var distanceCost: Int,
    var bookingFee: Int,
    var processingFee: Int,
    var subtotal: Int,
    var taxes: Int,
    var total: Int
) {
    val oilChangeTotal: Int get() {
        return oilChangeCost + distanceCost + bookingFee + processingFee
    }

    val discountTotal: Int get() {
        return 0 // TODO - missing fields?
    }

    val salesTaxTotal: Int get(){
        return taxes
    }

}
