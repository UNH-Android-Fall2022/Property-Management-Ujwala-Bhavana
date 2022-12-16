package com.property.management.owner.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.DocumentChange
import com.property.management.R
import com.property.management.databinding.FragmentNotificationsownerBinding

class NotificationsFragmentOwner : Fragment() {

    private var _binding: FragmentNotificationsownerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var nRecyclerView:RecyclerView
    private lateinit var nAdapter: NotificationAdapterOwner
    private lateinit var notificationList: ArrayList<Notification>
    private val db = Firebase.firestore
    private val channelId = "100"
    private val notificationId = 101


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsownerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        createNotificationChannel()
        notificationList = arrayListOf()
        nRecyclerView = binding.recyclerViewNotifications
        nRecyclerView.layoutManager = LinearLayoutManager(context)
        nAdapter = NotificationAdapterOwner(notificationList,this)
        nRecyclerView.adapter = nAdapter

        db.collection("Owners").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("Notifications")
            .addSnapshotListener{snapshot, e ->
                if(e != null){
                    return@addSnapshotListener
                }
                for(dc in snapshot!!.getDocumentChanges()){
                    if(dc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED)
                    {
                            sendNotification()
                    }

                }
                if(snapshot != null){
                    notificationList.clear()
                    for(notificationDocument in snapshot.documents){
                        val m = notificationDocument.getData()
                        notificationList.add(Notification(
                            m?.get("title").toString(),
                            m?.get("subject").toString()))
                    }

                    nAdapter.notifyDataSetChanged()

                }
            }



        return root
    }

    private fun sendNotification() {
        val contextTitle = " New Maintenance Request!!"
        var builder = NotificationCompat.Builder(requireContext(),channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(contextTitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireContext())) {
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel =
                NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}