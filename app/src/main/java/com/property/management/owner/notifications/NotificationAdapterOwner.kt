package com.property.management.owner.notifications

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.property.management.R
import com.property.management.tenant.notifications.NotificationAdapter

class NotificationAdapterOwner(private val notificationList:ArrayList<Notification>, val context: NotificationsFragmentOwner):
RecyclerView.Adapter<NotificationAdapterOwner.NotificationViewHolder>()
{
    class NotificationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val ntitle :TextView = itemView.findViewById(R.id.notificationTitle)
        val nsubject:TextView = itemView.findViewById(R.id.notificationSubject_owner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_notification,parent,false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.ntitle.text = notification.title
        holder.nsubject.text = notification.subject
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}