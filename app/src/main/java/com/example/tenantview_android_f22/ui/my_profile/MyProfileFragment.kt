package com.example.tenantview_android_f22.ui.my_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tenantview_android_f22.databinding.FragmentMyProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    val args: MyProfileFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private var duePaymentAmount = 0
    private var duePaymentDate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(MyProfileViewModel::class.java)

        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        readDataFromFirebase()
        binding.saveButton.setOnClickListener{
            writeDataToFirebase()
        }
        return root
    }
    private fun readDataFromFirebase(){
        db.collection("Tenant1")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d(TAG,"Tenant db from my profile fragment: ${document.id} => ${document.data}")
                    duePaymentAmount = document.data["duePaymentAmount"].toString().toInt()
                    duePaymentDate = document.data["duePaymentDate"].toString()
                    val tenantFirstName: TextView = binding.editFirstName
                    tenantFirstName.text = document.data["firstName"].toString()
                    val tenantLastName: TextView = binding.editLastName
                    tenantLastName.text = document.data["lastName"].toString()
                    val tenantEmail: TextView = binding.editEmailAddress
                    tenantEmail.text = document.data["emailID"].toString()
                    val tenantPhoneNum: TextView = binding.editPhoneNumber
                    tenantPhoneNum.text = document.data["phoneNum"].toString()
                }
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
    }
    private fun writeDataToFirebase(){
        val req = hashMapOf(
            "firstName" to binding.editFirstName.text.toString(),
            "lastName" to binding.editLastName.text.toString(),
            "emailID" to binding.editEmailAddress.text.toString(),
            "phoneNum" to binding.editPhoneNumber.text.toString(),
            "duePaymentAmount" to duePaymentAmount,
            "duePaymentDate" to duePaymentDate
        )

        db.collection("Tenant1").document(args.tenantID).set(req)
            .addOnSuccessListener { document ->
                Log.d(TAG,"Tenant details updated to collection: ${document}")
                val action = MyProfileFragmentDirections.actionMyProfileToNavigationAccount()
                findNavController().navigate(action)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG,"Error in writing document in Firebase",exception)
            }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}