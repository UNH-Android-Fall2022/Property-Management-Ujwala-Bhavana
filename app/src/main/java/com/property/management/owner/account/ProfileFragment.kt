package com.property.management.owner.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.databinding.FragmentProfileBinding

class ProfileFragment:Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val root: View = binding.root

        db.collection("Owners").document(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                val m = document.getData()
                binding.txtprofileName.setText(m?.get("Name").toString())
                binding.txtprofileEmail.setText(m?.get("Emailid").toString())
                binding.txtprofilePhone.setText(m?.get("PhoneNumber").toString())
            }
        return root
    }
}