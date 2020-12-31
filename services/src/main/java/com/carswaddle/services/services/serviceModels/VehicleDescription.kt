package com.carswaddle.carswaddleandroid.services.serviceModels

data class VehicleDescription(
    val id: String,
    val make: String,
    val model: String,
    val style: String,
    val trim: String,
    val year: String
)


//id: {
//    type: DataTypes.STRING,
//    primaryKey: true,
//    unique: true
//},
//make: {
//    type: DataTypes.STRING
//},
//model: {
//    type: DataTypes.STRING
//},
//style: {
//    type: DataTypes.STRING
//},
//trim: {
//    type: DataTypes.STRING
//},
//year: {
//    type: DataTypes.INTEGER,
//    allowNull: true
//}