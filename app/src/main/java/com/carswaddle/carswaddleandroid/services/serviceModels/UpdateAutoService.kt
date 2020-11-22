package com.carswaddle.carswaddleandroid.services.serviceModels

import org.json.JSONObject
import java.util.*

data class UpdateAutoService (
val status: String?,
val notes: String?,
val vehicleID: String?,
val mechanicID: String?,
val locationID: String?,
val scheduledDate: Date?,
val location: ServerLocation?
)



data class CreateAutoService (
    val status: AutoServiceStatus,
    val notes: String?,
    val vehicleID: String,
    val mechanicID: String,
    val scheduledDate: Date,
    val locationID: String?,
    val location: ServerLocation?,
    val serviceEntities: List<CreateServiceEntity>,
    val sourceID: String
) {
    companion object {
        fun oilChangeServiceEntities(oilType: OilType): List<CreateServiceEntity> {
            val oilChange = CreateServiceEntity.init(oilType)
            return listOf(oilChange)
        }
    }
}

/*

On server:

* const {
            status,
            scheduledDate,
            vehicleID,
            mechanicID,
            sourceID,
            serviceEntities,
            location: address,
            locationID,
            notes,
            couponID,
        } = req.body;
        
On iOS app:

var json: JSONObject = [:]
        
        if let locationID = location?.identifier {
            json["locationID"] = locationID
        } else if let location = location {
            json["location"] = location.toJSON()
        } else {
            throw StoreError.invalidJSON
        }
        
        if let mechanic = mechanic {
            json["mechanicID"] = mechanic.identifier
        } else {
            throw StoreError.invalidJSON
        }
        
        if let scheduledDate = scheduledDate {
            json["scheduledDate"] = serverDateFormatter.string(from: scheduledDate)
        } else {
            throw StoreError.invalidJSON
        }
        
        let jsonArray = serviceEntities.toJSONArray(includeIdentifiers: includeIdentifiers, includeEntityIdentifiers: false)
        if jsonArray.count > 0 {
            json["serviceEntities"] = jsonArray
        } else {
            throw StoreError.invalidJSON
        }
        
        if let vehicleID = vehicle?.identifier {
            json["vehicleID"] = vehicleID
        } else {
            throw StoreError.invalidJSON
        }
        
        json["status"] = status.rawValue
        json["notes"] = notes
        json["couponID"] = couponID
        
        return json
        
        
        
        Service entity:
        
        var entityJSON: JSONObject = [:]
        entityJSON["entityType"] = entityType.rawValue
        switch entityType {
        case .oilChange:
            if let oilChange = oilChange {
                entityJSON["specificService"] = oilChange.toJSON(includeIdentifier: includeEntityIdentifier)
            }
        }
        
        if includeIdentifier {
            entityJSON["identifier"] = identifier
        }
        
        return entityJSON
        
        
        oilChange:
        
        var json: JSONObject = [:]
        json["oilType"] = oilType.rawValue
        if includeIdentifier {
            json["identifier"] = identifier
        }
        return json
        
* */

//data class ServiceEntity(
//    val entityType: String,
//    val specificService: JSONObject
//)


data class ServerLocation(
    val longitude: Double,
    val latitude: Double
)
