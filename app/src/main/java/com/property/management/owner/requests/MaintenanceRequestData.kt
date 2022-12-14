package com.property.management.owner.requests

data class MaintenanceRequestData (
    var propertyName: String ="",
    var unitName:String ="",
    var subject: String ="",
    var description:String ="",
    var image: String ="",
    var status: String =""
    )
