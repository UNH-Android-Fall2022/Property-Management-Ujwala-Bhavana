package com.property.management.owner.properties.payments

import java.util.Date

data class PaymentData(
    var amount:Int,
    var paymentForMonth:String,
    var datePaid: String
){
}