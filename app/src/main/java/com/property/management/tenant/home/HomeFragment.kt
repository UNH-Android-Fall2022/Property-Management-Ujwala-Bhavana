package com.property.management.tenant.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.property.management.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private var auth = Firebase.auth
    private var tenantId = ""
    private val binding get() = _binding!!
    private var rentAmount = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val md = MessageDigest.getInstance("MD5")
        tenantId = md.digest(auth.currentUser!!.email!!.trim().toByteArray()).toHex()

        readTenantDataFromDB()

        binding.updateButton.setOnClickListener(){
            val action = HomeFragmentDirections.actionNavigationHomeToPaymentDetailsFragment()
            findNavController().navigate(action)
        }
        return root
    }
    private fun readTenantDataFromDB(){
        val paymentAmountView: TextView = binding.duePaymentAmount
        val paymentDueDateView: TextView = binding.showDueDate
        var formatDate = SimpleDateFormat("MMMM YYYY", Locale.US)
        val currentDate = Date()
        val currentMonthYear : String = formatDate.format(currentDate.time)
        var recordExists : Boolean = false
        var ifPaid : Boolean = false
        db.collection("Payments").whereEqualTo("tenantId",tenantId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    if(document.data["paymentForMonth"] == currentMonthYear ) {
                        Log.d(TAG, " Payment for month: ${document.data["paymentForMonth"]}")
                        recordExists = true
                        ifPaid = document.data["paid"] as Boolean
                    }
                }
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
        Log.d(TAG,"record: $recordExists, paid: $ifPaid, current month $currentMonthYear")
        db.collection("Tenants").document(tenantId)
            .get()
            .addOnSuccessListener { document ->

                    rentAmount = document.data?.get("rent").toString()
                    if(recordExists == true and ifPaid == true){
                        paymentAmountView.text = "0"
                        paymentDueDateView.text = "Yayy!! You have no current bills to pay."
                        binding.textForUpdatePaymentDetails.visibility = View.GONE
                        binding.updateButton.visibility = View.GONE
                    }
                    if (recordExists == true and ifPaid == false){
                        paymentAmountView.text = rentAmount
                        paymentDueDateView.text = "Owner is still reviewing the payment transaction..."
                        binding.textForUpdatePaymentDetails.visibility = View.GONE
                        binding.updateButton.visibility = View.GONE
                    }
                    if(recordExists == false){
                        paymentAmountView.text = rentAmount
                        paymentDueDateView.text = "05 $currentMonthYear"
                        binding.textForUpdatePaymentDetails.visibility = View.VISIBLE
                        binding.updateButton.visibility = View.VISIBLE
                    }

            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}