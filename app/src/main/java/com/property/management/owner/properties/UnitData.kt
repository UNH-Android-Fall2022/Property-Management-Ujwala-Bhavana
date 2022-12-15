package com.property.management.owner.properties

data class UnitData(
    var imgUrl:String = "image",
    var unitName:String,
    var unitType: String,
    var unitSize: Int,
    var tenantId: String = ""
        )
