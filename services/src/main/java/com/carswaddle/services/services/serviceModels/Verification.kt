package com.carswaddle.carswaddleandroid.services.serviceModels

import com.google.gson.annotations.SerializedName
import java.util.*

data class Verification(
    @SerializedName("disabled_reason") val disabledReason: String?,
    @SerializedName("current_deadline") val dueByDate: Calendar?,
    val mechanic: Mechanic?,
    @SerializedName("past_due") val pastDue: List<String>?,
    @SerializedName("currently_due") val currentlyDue: List<String>?,
    @SerializedName("eventually_due") val eventuallyDue: List<String>?
) {

    fun status(field: VerifyField): VerificationStatus {
        if (pastDue?.contains(field.name) == true) {
            return VerificationStatus.pastDue
        } else if (currentlyDue?.contains(field.name) == true) {
            return VerificationStatus.pastDue
        } else if (eventuallyDue?.contains(field.name) == true) {
            return VerificationStatus.pastDue
        } else {
            return VerificationStatus.notDue
        }
    }
    
    fun highestPriorityStatusForAddress(): VerificationStatus {
        val addressLine1Status = status(VerifyField.ADDRESS_LINE1)
        val addressCity = status(VerifyField.ADDRESS_CITY)
        val addressPostalCode = status(VerifyField.ADDRESS_POSTAL_CODE)
        val addressState = status(VerifyField.ADDRESS_STATE)
        
        if (addressLine1Status == VerificationStatus.pastDue || addressCity == VerificationStatus.pastDue || 
            addressPostalCode == VerificationStatus.pastDue || addressState == VerificationStatus.pastDue) {
            return VerificationStatus.pastDue
        } else if (addressLine1Status == VerificationStatus.currentlyDue || addressCity == VerificationStatus.currentlyDue ||
            addressPostalCode == VerificationStatus.currentlyDue || addressState == VerificationStatus.currentlyDue) {
            return VerificationStatus.currentlyDue
        } else if (addressLine1Status == VerificationStatus.eventuallyDue || addressCity == VerificationStatus.eventuallyDue ||
            addressPostalCode == VerificationStatus.eventuallyDue || addressState == VerificationStatus.eventuallyDue) {
            return VerificationStatus.eventuallyDue
        } else if (addressLine1Status == VerificationStatus.notDue || addressCity == VerificationStatus.notDue ||
            addressPostalCode == VerificationStatus.notDue || addressState == VerificationStatus.notDue) {
            return VerificationStatus.notDue
        }
        return VerificationStatus.notDue
    }

    companion object {
        val EXTERNAL_ACCOUNT = "external_account"
        val ADDRESS_CITY = "individual.address.city"
        val ADDRESS_LINE1 = "individual.address.line1"
        val ADDRESS_POSTAL_CODE = "individual.address.postal_code"
        val ADDRESS_STATE = "individual.address.state"
        val BUSINESS_NAME = "company.name"
        val BUSINESS_TAX_ID = "company.tax_id"
        val BIRTHDAY_DAY = "individual.dob.day"
        val BIRTHDAY_MONTH = "individual.dob.month"
        val BIRTHDAY_YEAR = "individual.dob.year"
        val FIRST_NAME = "individual.first_name"
        val LAST_NAME = "individual.last_name"
        val SOCIAL_SECURITY_NUMBER_LAST_4 = "individual.ssn_last_4"
        val TYPE = "individual.type"
        val TERMS_OF_SERVICE_ACCEPTANCE_DATE = "tos_acceptance.date"
        val TERMS_OF_SERVICE_IP_ADDRESS = "tos_acceptance.ip"
        val PERSONAL_ID_NUMBER = "individual.id_number"
        val VERIFICATION_DOCUMENT = "individual.verification.document"
    }


}

enum class VerificationStatus {
    pastDue,
    currentlyDue,
    eventuallyDue,
    notDue;
}

data class VerifyField(
    val name: String
) {
    companion object {
        val EXTERNAL_ACCOUNT = VerifyField("external_account")
        val ADDRESS_CITY = VerifyField("individual.address.city")
        val ADDRESS_LINE1 = VerifyField("individual.address.line1")
        val ADDRESS_POSTAL_CODE = VerifyField("individual.address.postal_code")
        val ADDRESS_STATE = VerifyField("individual.address.state")
        val BUSINESS_NAME = VerifyField("company.name")
        val BUSINESS_TAX_ID = VerifyField("company.tax_id")
        val BIRTHDAY_DAY = VerifyField("individual.dob.day")
        val BIRTHDAY_MONTH = VerifyField("individual.dob.month")
        val BIRTHDAY_YEAR = VerifyField("individual.dob.year")
        val FIRST_NAME = VerifyField("individual.first_name")
        val LAST_NAME = VerifyField("individual.last_name")
        val SOCIAL_SECURITY_NUMBER_LAST_4 = VerifyField("individual.ssn_last_4")
        val TYPE = VerifyField("individual.type")
        val TERMS_OF_SERVICE_ACCEPTANCE_DATE = VerifyField("tos_acceptance.date")
        val TERMS_OF_SERVICE_IP_ADDRESS = VerifyField("tos_acceptance.ip")
        val PERSONAL_ID_NUMBER = VerifyField("individual.id_number")
        val VERIFICATION_DOCUMENT = VerifyField("individual.verification.document")
    }
}
