package com.example.tenantview_android_f22.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tenantview_android_f22.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        db.collection("Tenant1")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d(TAG,"${document.id} => ${document.data}")

                    val paymentAmountView: TextView = binding.duePaymentAmount
                    paymentAmountView.text = document.data["duePaymentAmount"].toString()
                    val paymentDueDateView: TextView = binding.showDueDate

                    var tempAmt = document.data["duePaymentAmount"].toString().toInt()
                    if(tempAmt>0){
                        
                        paymentDueDateView.text = document.data["duePaymentDate"].toString()
                    }
                    else{

                        paymentDueDateView.text = "Yayy!! You have no current bills to pay."
                    }

                }
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}