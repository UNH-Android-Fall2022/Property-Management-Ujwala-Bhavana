package com.example.tenantview_android_f22.ui.contact_owner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tenantview_android_f22.databinding.FragmentContactOwnerBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContactOwnerFragment : Fragment() {

    private var _binding: FragmentContactOwnerBinding? = null

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
            ViewModelProvider(this).get(ContactOwnerViewModel::class.java)

        _binding = FragmentContactOwnerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        callOwnerFirebase()
        return root
    }
    private fun callOwnerFirebase(){
        //TODO: replace the tenant db
        val owner_id = "HXN3sUzjALeqRhB7et2GoiXtcoT2"
        db.collection("Owners").document(owner_id)
            .get()
            .addOnSuccessListener { document ->
                var ownerDetails = document.data
                val ownerName: TextView = binding.textOwnerNameValue
                ownerName.text = ownerDetails!!["Name"].toString()

                val ownerNumber : TextView = binding.textOwnerContactNumValue
                ownerNumber.text = ownerDetails!!["Phone Number"].toString()

                val ownerEmail : TextView = binding.textOwnerEmailIDValue
                ownerEmail.text = ownerDetails!!["Email id"].toString()
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