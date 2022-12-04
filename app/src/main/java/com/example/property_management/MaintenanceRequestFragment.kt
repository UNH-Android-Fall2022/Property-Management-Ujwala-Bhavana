package com.example.property_management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.property_management.databinding.FragmentMaintenancerequestBinding
import com.example.property_management.databinding.FragmentRequestsBinding

class MaintenanceRequestFragment:Fragment() {
    private var _binding: FragmentMaintenancerequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMaintenancerequestBinding.inflate(inflater,container,false)
        val root: View = binding.root

        return root
    }
}