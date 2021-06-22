package com.carswaddle.store.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.VerificationStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.VerifyField
import com.carswaddle.carswaddleandroid.services.serviceModels.Verification as ServiceVerification
import java.util.*


@Entity
data class Verification(
    @PrimaryKey val id: String,
    @ColumnInfo val disabledReason: String?,
    @ColumnInfo val dueByDate: Calendar?,
    @ColumnInfo val mechanicId: String?,
    @ColumnInfo val pastDue: List<String>?,
    @ColumnInfo val currentlyDue: List<String>?,
    @ColumnInfo val eventuallyDue: List<String>?

) {
    
    fun status(verifyField: VerifyField): VerificationStatus {
        if (pastDue?.contains(verifyField.name) == true) {
            return VerificationStatus.pastDue
        } else if (currentlyDue?.contains(verifyField.name) == true) {
            return VerificationStatus.pastDue
        } else if (eventuallyDue?.contains(verifyField.name) == true) {
            return VerificationStatus.pastDue
        } else {
            return VerificationStatus.notDue
        }
    }
    
    fun isAnyPersonalInformationDue(): Boolean {
        val address = highestPriorityStatusForAddress()
        val ss4 = status(VerifyField.SOCIAL_SECURITY_NUMBER_LAST_4)
        val fullSS = status(VerifyField.PERSONAL_ID_NUMBER)
        val bank = status(VerifyField.EXTERNAL_ACCOUNT)
        val doc = status(VerifyField.VERIFICATION_DOCUMENT)
        val birth = highestPriorityStatusForBirthday()
        
        val due = VerificationStatus.currentlyDue
        val past = VerificationStatus.pastDue
        
        return (address == due || address == past) || (ss4 == due || ss4 == past) || (fullSS == due || fullSS == past) || (bank == due || bank == past) || (doc == due || doc == past) || (birth == due || birth == past)
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

    fun highestPriorityStatusForBirthday(): VerificationStatus {
        val day = status(VerifyField.BIRTHDAY_DAY)
        val month = status(VerifyField.BIRTHDAY_MONTH)
        val year = status(VerifyField.BIRTHDAY_YEAR)

        if (day == VerificationStatus.pastDue || month == VerificationStatus.pastDue ||
            month == VerificationStatus.pastDue) {
            return VerificationStatus.pastDue
        } else if (day == VerificationStatus.currentlyDue || month == VerificationStatus.currentlyDue ||
            month == VerificationStatus.currentlyDue) {
            return VerificationStatus.currentlyDue
        } else if (day == VerificationStatus.eventuallyDue || month == VerificationStatus.eventuallyDue ||
            month == VerificationStatus.eventuallyDue) {
            return VerificationStatus.eventuallyDue
        } else if (day == VerificationStatus.notDue || month == VerificationStatus.notDue ||
            month == VerificationStatus.notDue) {
            return VerificationStatus.notDue
        }
        return VerificationStatus.notDue
    }
    
    constructor(verification: ServiceVerification, mechanicId: String) :
            this(
                "verification" + mechanicId,
                verification.disabledReason,
                verification.dueByDate,
                mechanicId,
                verification.pastDue,
                verification.currentlyDue,
                verification.eventuallyDue,
            )

}
