package com.property.management.tenant.maintenance_request

data class PastRequestData (
    var image: String,
    var subject: String,
    var description: String,
    var ownerid: String,
    var propertyname: String,
    var unitname: String,
    var status: String,
    var tenantid: String
)
