package com.example.property_management

data class UnitData (
    var imgURL:String = "image",
    var unitName:String,
    var unitType: String,
    var unitSize: Int,
    var propertyAddress:String
        )
val unitList:ArrayList<UnitData> = arrayListOf()
