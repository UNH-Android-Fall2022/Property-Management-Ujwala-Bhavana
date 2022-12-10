package com.property.management.tenant.maintenance_request

data class PastRequestData (
    var id: String,
    var image: String,
    var subject: String,
    var description: String,
    var ownerId: String,
    var propertyName: String,
    var unitName: String,
    var status: String,
    var tenantId: String
)
