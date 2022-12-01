package com.example.tenantview_android_f22.ui.notifications

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tenantview_android_f22.R


class NotificationAdapter(
    private val notificationList: ArrayList<NotificationData>,
    private val context: NotificationsFragment
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private val TAG = "Property_Management"
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.notificationSubject)
        val description : TextView = itemView.findViewById(R.id.notificationDescription)
        val delButton :Button = itemView.findViewById(R.id.deleteButton)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_notification_item_list, parent, false)
        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val req = notificationList[position]
        holder.subject.text = req.n_subject
        holder.description.text = req.n_description
        holder.delButton.setOnClickListener{view->
            Log.d(TAG,"delete button clicked")
            val action = NotificationsFragmentDirections.actionNotificationsFragmentSelf(req.n_id)
            view.findNavController().navigate(action)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int{
        return notificationList.size
    }

}