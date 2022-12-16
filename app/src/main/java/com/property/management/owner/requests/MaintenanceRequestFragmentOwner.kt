package com.property.management.owner.requests

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.GlideApp
import com.property.management.databinding.FragmentMaintenancerequestownerBinding

class MaintenanceRequestFragmentOwner:Fragment() {
    private var _binding: FragmentMaintenancerequestownerBinding? = null
    private val binding get() = _binding!!
    private val args : com.property.management.owner.requests.MaintenanceRequestFragmentOwnerArgs by navArgs()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMaintenancerequestownerBinding.inflate(inflater,container,false)
        val root: View = binding.root
        val propertyname = args.propertyname
        val unitname = args.unitname
        val subject = args.subject
        val description = args.description
        val imgurl = args.imgURL
        val docId = args.docId
        Log.d("Maintenace Fragment","$unitname")

        binding.textpropertyname.text = propertyname
        binding.textunitname.text = unitname
        binding.textsubject.text = subject
        binding.textdescription.text = description
        GlideApp.with(requireContext()).load(imgurl).into(binding.requestImage)


        binding.closerequestbtn.setOnClickListener {
            db.collection("Maintenance Request").document(docId).update("status","closed")
        }

        return root
    }
}