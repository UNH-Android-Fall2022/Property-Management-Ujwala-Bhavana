package com.property.management.tenant.chat_with_owner

import java.util.*

data class MessageDataTenant(
    val message: String,
    val senderId: String,
    val timestamp: Date?
) {
}