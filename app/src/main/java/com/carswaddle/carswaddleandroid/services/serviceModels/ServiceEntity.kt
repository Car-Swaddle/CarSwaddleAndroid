package com.carswaddle.carswaddleandroid.services.serviceModels

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject
import java.util.*

data class ServiceEntity (
    val id: String,
    val entityType: ServiceEntityType,
    val createdAt: Date,
    val updatedAt: Date,
    val autoServiceID: String,
    val oilChangeID: String,
    val oilChange: OilChange
)



data class CreateServiceEntity(
    val entityType: ServiceEntityType,
    val specificService: JSONObject // Generic json that will include the details of the service entity, currently only oil changes are supported
) {
    
    companion object {
        
        fun init(oilType: OilType): CreateServiceEntity {
            return init(UploadOilChange(oilType))
        }
        
        fun init(oilChange: UploadOilChange): CreateServiceEntity {
            val oilChangeJSONString = Gson().toJson(oilChange)
            var oilChangeJSON = JSONObject()
            try {
                oilChangeJSON = JSONObject(oilChangeJSONString)
            } finally { }
            return CreateServiceEntity(ServiceEntityType.OIL_CHANGE, oilChangeJSON)
        }
        
    }
    
}

enum class ServiceEntityType {
    OIL_CHANGE
}