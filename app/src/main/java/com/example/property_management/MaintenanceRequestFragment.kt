package com.example.property_management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.property_management.databinding.FragmentMaintenancerequestBinding
import com.example.property_management.databinding.FragmentRequestsBinding

class MaintenanceRequestFragment:Fragment() {
    private var _binding: FragmentMaintenancerequestBinding? = null
    private val binding get() = _binding!!
    private val args : MaintenanceRequestFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMaintenancerequestBinding.inflate(inflater,container,false)
        val root: View = binding.root
        val propertyname = args.propertyname
        val unitname = args.unitname
        val subject = args.subject
        val description = args.description
        val imgurl = args.imgURL
        Log.d("Maintenace Fragment","$unitname")

        binding.textpropertyname.text = propertyname
        binding.textunitname.text = unitname
        binding.textsubject.text = subject
        binding.textdescription.text = description
        return root
    }
}