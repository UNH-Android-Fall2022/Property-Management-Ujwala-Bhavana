package com.example.tenantview_android_f22.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenantview_android_f22.R
import com.example.tenantview_android_f22.databinding.FragmentNotificationsBinding
import com.example.tenantview_android_f22.ui.maintenance_request.PastRequestAdapter
import com.example.tenantview_android_f22.ui.maintenance_request.PastRequestData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private val channelId = "channel_id_01"
    private val notificationId = 101
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var listOfNotificationRequests: ArrayList<NotificationData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        createNotificationChannel()
        binding.sendButton.setOnClickListener{
            sendNotification()
        }
        listOfNotificationRequests = arrayListOf()
        readFromFirestore()
        return root
    }
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel : NotificationChannel = NotificationChannel(channelId,name,importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        var builder = NotificationCompat.Builder(requireContext(),channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireContext())){
            notify(notificationId,builder.build())
        }
    }
    private fun readFromFirestore(){
        val doc_id = "Hm45sgO2foCFweCYhNmn"
        db.collection("Tenant1").document(doc_id).collection("Notification")
            .get()
            .addOnCompleteListener { snapshot ->
                for (document in snapshot.result) {
                    Log.d(TAG, "${document.getData()}")
                    val temp = document.getData()
                    val req: NotificationData = NotificationData(
                        n_id = document.id,
                        temp.get("subject").toString(), temp.get("description").toString()
                    )
                    listOfNotificationRequests.add(req)
                }
                mRecyclerView = binding.notificationRecyclerViewList
                mRecyclerView.layoutManager = LinearLayoutManager(context)
                mRecyclerView.adapter = NotificationAdapter(listOfNotificationRequests, this)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents", exception)
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}