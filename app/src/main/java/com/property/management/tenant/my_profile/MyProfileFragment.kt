package com.property.management.tenant.my_profile

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
import com.property.management.databinding.FragmentMyProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.tenant.maintenance_request.PastRequestData

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    val args: MyProfileFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore


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
        db.collection("Tenants").document(args.tenantID)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(TAG,"Called Tenant db from my profile fragment:")

                val tenantFirstName: TextView = binding.editFirstName
                tenantFirstName.text = documents.data?.get("firstName").toString()
                val tenantLastName: TextView = binding.editLastName
                tenantLastName.text = documents.data?.get("lastName").toString()
                val tenantEmail: TextView = binding.editEmailAddress
                tenantEmail.text = documents.data?.get("email").toString()
                val tenantPhoneNum: TextView = binding.editPhoneNumber
                tenantPhoneNum.text = documents.data?.get("phoneNumber").toString()
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
    }
    private fun writeDataToFirebase(){

            val firstName = binding.editFirstName.text.toString()
            val lastName =  binding.editLastName.text.toString()
            val emailID = binding.editEmailAddress.text.toString()
            val phoneNum = binding.editPhoneNumber.text.toString()


        db.collection("Tenants").document(args.tenantID).update("firstName",firstName,"lastName",lastName,"email",emailID,"phoneNumber",phoneNum)
            .addOnSuccessListener { document ->
                Log.d(TAG,"Tenant details updated to collection: $document")
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