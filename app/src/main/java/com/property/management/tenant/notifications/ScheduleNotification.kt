package com.property.management.tenant.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.R

const val channelId = "channel_id_01"
const val notificationId = 101
const val text = ""

class ScheduleNotification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent : Intent) {
        val builder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("Payment Due!!")
            .setContentText(intent.getStringExtra(text))
        with(NotificationManagerCompat.from(context)){
            notify(notificationId,builder.build())
        }
    }
}