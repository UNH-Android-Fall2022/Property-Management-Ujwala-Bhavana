package com.property.management.owner.requests

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.property.management.databinding.FragmentMaintenancerequestBinding

class MaintenanceRequestFragment:Fragment() {
    private var _binding: FragmentMaintenancerequestBinding? = null
    private val binding get() = _binding!!
    private val args : com.property.management.owner.requests.MaintenanceRequestFragmentArgs by navArgs()

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