package com.property.management.owner.requests

data class MaintenanceRequestData (
    var propertyname: String ="",
    var unitname:String ="",
    var subject: String ="",
    var description:String ="",
    var imgUrl: String ="",
    var status: String =""
    )
