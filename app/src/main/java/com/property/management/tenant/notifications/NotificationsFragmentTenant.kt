package com.property.management.tenant.notifications

import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.management.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.AlarmManager
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
        notificationFirestore("read")
        var dueAmount = args.rentAmount
        var formatDate = SimpleDateFormat("MMMM YYYY", Locale.US)
        val currentDate = Date()
        val currentMonthYear : String = formatDate.format(currentDate.time)
        contextTitle = "Payment Due!!"
        contextText = "Your rent payment $$dueAmount is due. Please make the payment by 05 $currentMonthYear."
        createNotificationChannel()
        scheduleNotification()
        listOfNotificationRequests = arrayListOf()

        
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
    private fun scheduleNotification(){
        val intent = Intent(context,ScheduleNotification::class.java)
        intent.putExtra(text,contextText)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR,Calendar.MONTH,15,14,59)
        val time = calendar.timeInMillis
        alarmManager.setAndAllowWhileIdle(
            RTC_WAKEUP,
            time,
            pendingIntent
        )

        Log.d(TAG,"Notification Scheduled")

        notificationFirestore("insert")
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
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}