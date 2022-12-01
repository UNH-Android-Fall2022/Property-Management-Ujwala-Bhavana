package com.example.tenantview_android_f22.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tenantview_android_f22.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        Log.d(TAG,"Calling the tenant database...")
        readTenantDataFromDB()
        binding.updateButton.setOnClickListener(){

            val action = HomeFragmentDirections.actionNavigationHomeToPaymentDetailsFragment(rentAmount)
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
        db.collection("Payments")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    if(document.data["paymentForMonth"]==currentMonthYear){
                        recordExists  = true
                        ifPaid = document.data["paid"] as Boolean
                    }
                }}
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
        db.collection("Tenant1")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d(TAG,"${document.id} => ${document.data}")
                    rentAmount = document.data["duePaymentAmount"].toString()
                    if(recordExists == true and ifPaid == true){
                        paymentAmountView.text = "0"
                        paymentDueDateView.text = "Yayy!! You have no current bills to pay."
                    }
                    if (recordExists == true and ifPaid == false){
                        paymentAmountView.text = document.data["duePaymentAmount"].toString()
                        paymentDueDateView.text = "Owner is still reviewing the payment transaction..."
                        binding.textForUpdatePaymentDetails
                    }
                    if(recordExists == false){
                        paymentAmountView.text = document.data["duePaymentAmount"].toString()
                        paymentDueDateView.text = "05 $currentMonthYear"
                    }
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
}