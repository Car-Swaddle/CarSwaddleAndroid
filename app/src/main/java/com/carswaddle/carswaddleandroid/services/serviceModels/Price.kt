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
    var discount: Int?, // The discount on the mechanic's portion of the transaction
    var bookingFeeDiscount: Int?, // The discount on the Car Swaddle portion of the transaction
    var total: Int
) {
    
    val oilChangeTotal: Int get() {
        return subtotal + processingFee + bookingFee
    }

    val discountTotal: Int? get() {
        if  (bookingFeeDiscount == null && discount == null) {
            return null
        }

        var finalTotalDiscount: Int = 0
        
        val cDiscount = discount
        if (cDiscount != null) {
            finalTotalDiscount = finalTotalDiscount + cDiscount 
        }

        val bDiscount = bookingFeeDiscount
        if (bDiscount != null) {
            finalTotalDiscount = finalTotalDiscount + bDiscount
        }

        return finalTotalDiscount
    }

    val salesTaxTotal: Int get(){
        return taxes
    }

}
