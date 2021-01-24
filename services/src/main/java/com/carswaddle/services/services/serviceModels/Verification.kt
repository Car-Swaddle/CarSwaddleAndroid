package com.carswaddle.carswaddleandroid.services.serviceModels

import com.google.gson.annotations.SerializedName
import java.util.*

data class Verification (
    val disabledReason: String?,
    val dueByDate: Calendar?,
    val mechanic: Mechanic?,
    val pastDue: List<String>?,
    val currentlyDue: List<String>?,
    val eventuallyDue: List<String>?
) {
    
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
