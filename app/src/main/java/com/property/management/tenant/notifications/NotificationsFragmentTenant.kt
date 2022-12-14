package com.property.management.tenant.notifications

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.management.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.databinding.FragmentNotificationstenantBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationsFragmentTenant : Fragment() {

    private var _binding: FragmentNotificationstenantBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private val args : NotificationsFragmentTenantArgs by navArgs()
    private val channelId = "channel_id_01"
    private val notificationId = 101
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var listOfNotificationRequests: ArrayList<NotificationData>
    private var contextTitle = ""
    private var contextText = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationstenantBinding.inflate(inflater, container, false)
        val root: View = binding.root
        createNotificationChannel()
        binding.sendButton.setOnClickListener{
            sendPaymentReminderNotification()
        }
        listOfNotificationRequests = arrayListOf()
        notificationFirestore("read")
        
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
    private fun sendPaymentReminderNotification(){
        db.collection("Tenants").document(args.tenantID)
            .get()
            .addOnSuccessListener { document ->
                var dueAmount = document.data?.get("rent").toString()
                var formatDate = SimpleDateFormat("MMMM YYYY", Locale.US)
                val currentDate = Date()
                val currentMonthYear : String = formatDate.format(currentDate.time)
                contextTitle = "Payment Due!!"
                contextText = "Your rent payment $$dueAmount is due. Please make the payment by 05 $currentMonthYear."
                var builder = NotificationCompat.Builder(requireContext(),channelId)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle(contextTitle)
                    .setContentText(contextText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(requireContext())){
                    notify(notificationId,builder.build())
                }
                notificationFirestore("insert")
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
    }
    private fun notificationFirestore(action : String) {
        if (action == "read") {
        db.collection("Notifications").whereEqualTo("userID",args.tenantID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val req: NotificationData = NotificationData(
                        n_id = document.id,
                        document.data["subject"].toString(),
                        document.data["description"].toString()
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
        else if(action == "insert"){
            val req = hashMapOf(
                "subject" to contextTitle,
                "description" to contextText,
                "userID" to args.tenantID
            )
            db.collection("Notifications").add(req)
                .addOnSuccessListener { document ->
                    Log.d(TAG,"Notification added to collection: ${document.id}")
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG,"Error in writing document in Firebase",exception)
                }
        }
        else if(action == "delete"){
            /*db.collection("Tenant1").document(doc_id).collection("Notification").document(args.notificationID).delete()
                .addOnSuccessListener { document ->
                    Log.d(TAG,"Notification deleted")
                    val action = NotificationsFragmentDirections.actionNotificationsFragmentSelf("")
                    findNavController().navigate(action)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG,"Error in writing document in Firebase",exception)
                }*/
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}