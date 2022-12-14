package com.property.management.owner.chats

import java.sql.Timestamp
import java.util.Date

data class MessageData (
    val message: String,
    val senderId: String,
    val timestamp: Date?
)
