package com.carswaddle.carswaddleandroid.services.serviceModels

import com.google.gson.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap


data class ServiceEntity(
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
            var oilChangeJSON: JSONObject = JSONObject()
            try {
                oilChangeJSON = JSONObject(oilChangeJSONString)
            } finally { }
            return CreateServiceEntity(ServiceEntityType.OIL_CHANGE, oilChangeJSON)
        }
        
    }
    
}



class CreateServiceEntitySerializer : JsonSerializer<CreateServiceEntity?> {
    
    override fun serialize(
        src: CreateServiceEntity?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        
        if (src == null) {
            return JsonObject()
        }

        val obj = JsonObject()

        obj.addProperty("entityType", src.entityType.toString())

        val map: MutableMap<String, Any> = mutableMapOf()
        
        val iter: Iterator<String> = src.specificService.keys()
        while (iter.hasNext()) {
            val key = iter.next()
            try {
                val value: Any = src.specificService.get(key)
                map.set(key, value)
            } catch (e: JSONException) {
                print("something went wrong")
            }
        }
        
        val gson = Gson()
        val jsonElement = gson.toJsonTree(map)
        obj.add("specificService", jsonElement)
        
        return obj
    }
}


enum class ServiceEntityType {
    OIL_CHANGE
}